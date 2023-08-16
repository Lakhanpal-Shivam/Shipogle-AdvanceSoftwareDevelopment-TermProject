package com.shipogle.app.controller;

import com.shipogle.app.model.ChatMessageRequest;
import com.shipogle.app.model.Message;
import com.shipogle.app.model.User;
import com.shipogle.app.repository.MessageRepository;
import com.shipogle.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@Transactional
@RequestMapping("/chat")
public class ChatController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageRepository messageRepository;

    /**
     * Send message
     *
     * @author Rahul Saliya
     * @param request request.
     * @return ResponseEntity.
     */
    @PostMapping
    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public ResponseEntity<?> sendMessage(@RequestBody ChatMessageRequest request) {
        Optional<User> senderOptional = userRepository.findById(request.getSenderId());
        Optional<User> receiverOptional = userRepository.findById(request.getReceiverId());
        if (senderOptional.isEmpty() || receiverOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid sender or receiver ID");
        }

        User sender = senderOptional.get();
        User receiver = receiverOptional.get();

        Message message = new Message();
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setMessage(request.getMessage());
        message.setCreatedAt(LocalDateTime.now());

        messageRepository.save(message);

        return ResponseEntity.ok().build();
    }

    /**
     * Get chat history
     *
     * @author Rahul Saliya
     * @param senderId   sender id.
     * @param receiverId receiver id.
     * @return List of messages.
     */
    @GetMapping("/{senderId}/{receiverId}")
    public List<Message> getChatHistory(@PathVariable int senderId, @PathVariable int receiverId) {
        Optional<User> senderOptional = userRepository.findById(senderId);
        Optional<User> receiverOptional = userRepository.findById(receiverId);
        if (senderOptional.isEmpty() || receiverOptional.isEmpty()) {
            throw new RuntimeException("Invalid sender or receiver ID");
        }

        User sender = senderOptional.get();
        User receiver = receiverOptional.get();

        List<Message> messages = new ArrayList<>();
        messages.addAll(messageRepository.findBySenderAndReceiverOrderByCreatedAtDesc(sender, receiver));
        messages.addAll(messageRepository.findByReceiverAndSenderOrderByCreatedAtDesc(sender, receiver));
        messages.sort(Comparator.comparing(Message::getCreatedAt));

        return messages;
    }

    /**
     * Get chat users
     *
     * @author Rahul Saliya
     * @param userId user id.
     * @return List of users.
     */
    @GetMapping("/{userId}")
    public List<User> getChatUsers(@PathVariable int userId) {
        Optional<User> receiverOptional = userRepository.findById(userId);
        if (receiverOptional.isEmpty()) {
            throw new RuntimeException("Invalid user ID");
        }

        User user = receiverOptional.get();

        List<Integer> userIds =
                messageRepository.findDistinctSenderAndReceiverIdsByUserId(user.getUser_id());

        return userRepository.getUserByIds(userIds);
    }

    /**
     * Remove message
     *
     * @author Rahul Saliya
     * @param messageId message id.
     * @return ResponseEntity.
     */
    @DeleteMapping("/{messageId}")
    public ResponseEntity<?> removeMessage(@PathVariable long messageId) {
        messageRepository.deleteById(messageId);
        return ResponseEntity.ok("Message deleted successfully");
    }

    /**
     * Remove all messages
     *
     * @author Rahul Saliya
     * @param receiverId receiver id.
     * @param senderId   sender id.
     * @return ResponseEntity.
     */
    @DeleteMapping("/all/{receiverId}/{senderId}")
    public ResponseEntity<?> removeAllMessages(@PathVariable int receiverId, @PathVariable int senderId) {
        Optional<User> senderOptional = userRepository.findById(senderId);
        Optional<User> receiverOptional = userRepository.findById(receiverId);
        if (senderOptional.isEmpty() || receiverOptional.isEmpty()) {
            throw new RuntimeException("Invalid sender or receiver ID");
        }

        User receiver = receiverOptional.get();
        User sender = senderOptional.get();

        messageRepository.deleteMessagesByReceiverAndSender(receiver, sender);
        messageRepository.deleteMessagesByReceiverAndSender(sender, receiver);
        return ResponseEntity.ok("Messages deleted successfully");
    }




}
