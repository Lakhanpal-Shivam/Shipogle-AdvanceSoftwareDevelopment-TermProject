package com.shipogle.app.repository;

import com.shipogle.app.model.Message;
import com.shipogle.app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    /**
     * findBySenderAndReceiverOrderByCreatedAtDesc is a method to get messages by sender and receiver order by created at desc
     *
     * @author Rahul Saliya
     * @param sender sender object
     * @param receiver receiver object
     * @return List<Message>
     */
    List<Message> findBySenderAndReceiverOrderByCreatedAtDesc(User sender, User receiver);

    /**
     * findByReceiverAndSenderOrderByCreatedAtDesc is a method to get messages by receiver and sender order by created at desc
     *
     * @author Rahul Saliya
     * @param receiver receiver object
     * @param sender sender object
     * @return List<Message>
     */
    List<Message> findByReceiverAndSenderOrderByCreatedAtDesc(User receiver, User sender);

    /**
     * findDistinctSenderAndReceiverIdsByUserId is a method to get distinct sender and receiver ids by user id
     *
     * @author Rahul Saliya
     * @param userId user id
     * @return List<Integer>
     */
    @Query(value = "SELECT DISTINCT m.receiver_id as id FROM messages as m WHERE m.sender_id = :userId UNION SELECT DISTINCT m.sender_id as id FROM messages as m WHERE m.receiver_id = :userId", nativeQuery = true)
    List<Integer> findDistinctSenderAndReceiverIdsByUserId(@Param("userId") Integer userId);

    /**
     * deleteMessagesBySenderAndReceiver is a method to delete messages by sender and receiver
     *
     * @author Rahul Saliya
     * @param sender sender object
     * @param receiver receiver object
     */
    void deleteMessagesByReceiverAndSender(User receiver, User sender);

}
