package com.shipogle.app.controller;

import com.shipogle.app.TestConstants;
import com.shipogle.app.model.Notification;
import com.shipogle.app.model.User;
import com.shipogle.app.repository.NotificationRepository;
import com.shipogle.app.repository.UserRepository;
import com.shipogle.app.service.AuthService;
import com.shipogle.app.socket_handlers.NotificationSocketHandler;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.crypto.spec.SecretKeySpec;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static com.shipogle.app.utility.Const.SECRET_KEY;
import static com.shipogle.app.utility.Const.TOKEN_EXPIRATION_TIME;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


public class NotificationControllerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private UserRepository userRepo;

    @Mock
    private AuthService authService;

    @InjectMocks
    private NotificationController notificationController;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    final private int NOTIFICATION_USER_ID = 12345;

    @Test
    public void testSendNotification_Success() {
        // Given
        Map<String, String> json = new HashMap<>();
        int userId = 1;
        json.put("userId", String.valueOf(userId));
        json.put("title", "Test Notification");
        json.put("message", "This is a test notification message");
        json.put("payload", "{\"key\": \"value\"}");
        json.put("type", "test");

        User user = new User();
        user.setId(userId);
        user.setEmail("test@test.com");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        Notification notification = new Notification();
        notification.setId(1L);
        notification.setUser(user);
        notification.setTitle("Test Notification");
        notification.setMessage("This is a test notification message");
        notification.setPayload("{\"key\": \"value\"}");
        notification.setType("test");
        notification.setCreatedAt(LocalDateTime.now());

        when(notificationRepository.save(any(Notification.class))).thenReturn(notification);

        NotificationSocketHandler notificationSocketHandler = mock(NotificationSocketHandler.class);

        doNothing()
                .when(notificationSocketHandler)
                .sendNotification(user.getUser_id(), notification);

        // When
        ResponseEntity<?> responseEntity = notificationController.sendNotification(json);

        System.out.println("responseEntity = " + responseEntity.getBody());
        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void testSendNotification_InvalidUserId() {
        // Given
        Map<String, String> json = new HashMap<>();
        json.put("userId", "2");
        json.put("title", "Test Notification");
        json.put("message", "This is a test notification message");
        json.put("payload", "{\"key\": \"value\"}");
        json.put("type", "test");

        when(userRepository.findById(TestConstants.USER_ONE_ID)).thenReturn(Optional.empty());

        // When
        ResponseEntity<?> responseEntity = notificationController.sendNotification(json);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Invalid sender or receiver ID", responseEntity.getBody());
    }

    @Test
    public void testSendNotification_InvalidUserInfo() {
        // Given
        Map<String, String> json = new HashMap<>();
        json.put("userId", null);
        json.put("title", "Test Notification");
        json.put("message", "This is a test notification message");
        json.put("payload", "{\"key\": \"value\"}");
        json.put("type", "test");

        User user = new User();
        user.setId(TestConstants.USER_ONE_ID);
        user.setEmail("test@test.com");

        when(userRepository.findById(TestConstants.USER_ONE_ID)).thenReturn(Optional.of(user));
        when(authService.getUserInfo(anyString())).thenReturn(null);

        // When
        ResponseEntity<?> responseEntity = notificationController.sendNotification(json);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Invalid user ID", responseEntity.getBody());
    }

    @Test
    public void testGetNotificationsByInvalidToken() {
        // Create a mock authorization token for the user
        String token = "Bearer wrong_token";


        assertThrows(RuntimeException.class, () -> notificationController.getNotificationsByToken(token));
    }

    @Test
    public void testGetNotificationsByUserId() {
        // Create a mock user and add it to the repository
        User user = new User();
        user.setUser_Id(TestConstants.USER_ONE_ID);
        userRepository.save(user);

        // Create a mock notification and add it to the repository
        Notification notification = new Notification();
        notification.setUser(user);
        notificationRepository.save(notification);

        when(userRepository.findById(TestConstants.USER_ONE_ID)).thenReturn(Optional.of(user));
        when(notificationController.getNotifications(user.getUser_id())).thenReturn(Collections.singletonList(notification));

        // Get the notifications using the user ID and check the response
        List<Notification> notifications = notificationController.getNotifications(user.getUser_id());
        assertEquals(1, notifications.size());
        assertEquals(notification, notifications.get(0));
    }

    @Test
    public void testDeleteNotificationsByUserId() {
        // Create a mock user and add it to the repository
        User user = new User();
        user.setUser_Id(TestConstants.USER_ONE_ID);
        userRepository.save(user);

        // Create a mock notification and add it to the repository
        Notification notification = new Notification();
        notification.setUser(user);
        notificationRepository.save(notification);

        when(userRepository.findById(TestConstants.USER_ONE_ID)).thenReturn(Optional.of(user));
        when(notificationController.getNotifications(user.getUser_id())).thenReturn(Collections.singletonList(notification));

        // Get the notifications using the user ID and check the response
        List<Notification> notifications = notificationController.getNotifications(user.getUser_id());
        assertEquals(1, notifications.size());
        assertEquals(notification, notifications.get(0));

        notificationController.deleteNotifications(user.getUser_id());
        when(notificationController.getNotifications(user.getUser_id())).thenReturn(new ArrayList<>());

        // Get the notifications using the user ID and check the response
        List<Notification> notifications_new = notificationController.getNotifications(user.getUser_id());
        assertEquals(0, notifications_new.size());
    }

    @Test
    public void testDeleteNotificationsByInvalidUserId() {
        when(userRepository.findById(TestConstants.USER_ONE_ID)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> notificationController.deleteNotifications(NOTIFICATION_USER_ID));
    }

    @Test
    public void testGetNotificationsByInvalidUserId() {
        // Try to get the notifications using an invalid user ID and check the response
        assertThrows(RuntimeException.class, () -> notificationController.getNotifications(NOTIFICATION_USER_ID));
    }

    @Test
    public void testSendNotificationWithValidUserId() {
        int userId = 1;
        User user = new User();
        user.setUser_Id(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        Map<String, String> json = new HashMap<>();
        json.put("userId", String.valueOf(userId));
        ResponseEntity<?> responseEntity = notificationController.sendNotification(json);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void testSendNotificationWithInvalidUserId() {
        int userId = 1;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        Map<String, String> json = new HashMap<>();
        json.put("userId", String.valueOf(userId));
        ResponseEntity<?> responseEntity = notificationController.sendNotification(json);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

}