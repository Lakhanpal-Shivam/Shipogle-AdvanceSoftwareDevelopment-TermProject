package com.shipogle.app.controller;

import com.shipogle.app.model.Notification;
import com.shipogle.app.model.User;
import com.shipogle.app.repository.NotificationRepository;
import com.shipogle.app.repository.UserRepository;
import com.shipogle.app.service.AuthServiceImpl;
import com.shipogle.app.service.AuthService;
import com.shipogle.app.socket_handlers.NotificationSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@Transactional
@RequestMapping("/notifications")
public class NotificationController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private AuthService authService;

    /**
     * Get all notifications
     *
     * @author Rahul Saliya
     * @param json json data
     * @return list of notifications
     */
    @PostMapping
    public ResponseEntity<?> sendNotification(@RequestBody Map<String, String> json) {
        System.out.println("json = " + json);
        try {
            String userId = json.get("userId");
            String title = json.getOrDefault("title", "Notification");
            String message = json.getOrDefault("message", "");
            String payload = json.getOrDefault("payload", "{}");
            String type = json.getOrDefault("type", "default");

            Optional<User> userOptional = userRepository.findById(Integer.parseInt(userId));
            if (userOptional.isEmpty()) {
                return ResponseEntity.badRequest().body("Invalid sender or receiver ID");
            }

            User user = userOptional.get();

            Notification notification = new Notification();
            notification.setUser(user);
            notification.setTitle(title);
            notification.setMessage(message);
            notification.setPayload(payload);
            notification.setType(type);
            notification.setCreatedAt(LocalDateTime.now());

            notificationRepository.save(notification);
            System.out.println("notification = " + notification);
            NotificationSocketHandler.getInstance().sendNotification(user.getUser_id(),notification);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Invalid user ID");
        }
    }

    /**
     * Get all notifications by token
     *
     * @author Rahul Saliya
     * @param token token
     * @return list of notifications
     */
    @PostMapping("/get")
    public List<Notification> getNotificationsByToken(@RequestHeader("Authorization") String token) {
        User user = new AuthServiceImpl().getUserInfo(token);
        if (user == null) {
            throw new RuntimeException("Invalid user ID");
        }

        return new ArrayList<>(notificationRepository.findByUserOrderByCreatedAt(user));
    }

    /**
     * Get all notifications by user id
     *
     * @author Rahul Saliya
     * @param userId user id
     * @return list of notifications
     */
    @GetMapping("/{userId}")
    public List<Notification> getNotifications(@PathVariable int userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new RuntimeException("Invalid user ID");
        }

        User user = userOptional.get();

        return new ArrayList<>(notificationRepository.findByUserOrderByCreatedAt(user));
    }

    /**
     * Delete all notifications by user id
     *
     * @author Rahul Saliya
     * @param userId user id
     * @return response entity
     */
    @DeleteMapping("/all/{userId}")
    public ResponseEntity<?> deleteNotifications(@PathVariable int userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new RuntimeException("Invalid user ID");
        }

        User user = userOptional.get();

        notificationRepository.deleteNotificationsByUser(user);

        return ResponseEntity.ok("Deleted notifications for user with id: " + userId);
    }

}
