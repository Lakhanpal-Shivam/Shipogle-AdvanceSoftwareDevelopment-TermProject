package com.shipogle.app.repository;

import com.shipogle.app.model.Notification;
import com.shipogle.app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotificationRepository  extends JpaRepository<Notification, Long> {

    /**
     * findByUserOrderByCreatedAt is a method to get notifications by user order by created at
     *
     * @author Rahul Saliya
     * @param user user object
     * @return List<Notification>
     */
    List<Notification> findByUserOrderByCreatedAt(@Param("user") User user);

    /**
     * deleteNotificationsByUser is a method to delete notifications by user
     *
     * @author Rahul Saliya
     * @param user user object
     */
    void deleteNotificationsByUser(User user);
}
