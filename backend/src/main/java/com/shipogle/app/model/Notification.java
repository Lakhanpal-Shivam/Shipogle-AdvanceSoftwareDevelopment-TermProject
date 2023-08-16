package com.shipogle.app.model;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Notification model.
 *
 * @author Rahul Saliya
 */
@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    private String message;

    private String title;

    private String payload;

    private String type;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public void setId(Long id) {
        this.id = id;
    }

    public void setUser(User user) {
        this.user = user;
    }


    public void setTitle(String title) {
        this.title = title;
    }


    public void setMessage(String message) {
        this.message = message;
    }


    public void setPayload(String payload) {
        this.payload = payload;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public String getTitle() {
        return title;
    }

    public String getPayload() {
        return payload;
    }

    public String getType() {
        return type;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Integer getUserId() {
        return user.getUser_id();
    }

    @Override
    public String toString() {
        return String.format("{ " +
                "\"id\":%d," +
                "\"userId\":\"%s\"," +
                "\"title\":\"%s\"," +
                "\"message\":\"%s\"," +
                "\"payload\":\"%s\"," +
                "\"type\":\"%s\"," +
                "\"created_at\":\"%s\" }", id, user.getUser_id(), title, message, payload, type, createdAt);
    }
}
