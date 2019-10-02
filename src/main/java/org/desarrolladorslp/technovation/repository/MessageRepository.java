package org.desarrolladorslp.technovation.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.desarrolladorslp.technovation.models.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface MessageRepository extends JpaRepository<Message, UUID> {

    @Query(value = "SELECT * FROM messages AS m INNER JOIN messages_by_users AS mbu ON m.id = mbu.message_id WHERE mbu.user_receiver_id = :userReceiverId AND high_priority = :highPriority", nativeQuery = true)
    List<Message> getMessagesByPriority(UUID userReceiverId, Boolean highPriority);

    @Query("SELECT m FROM Message m INNER JOIN MessagesByUser mbu ON m.id = mbu.messageId WHERE m.id = :messageId AND mbu.userReceiverId = :userReceiverId")
    Optional<Message> getBodyOfMessageByUser(UUID messageId, UUID userReceiverId);

    @Modifying
    @Query(value = "UPDATE messages SET read = true WHERE id = :messageId", nativeQuery = true)
    void markMessageAsRead(UUID messageId);

    @Modifying
    @Query(value = "UPDATE messages SET read = false WHERE id = :messageId", nativeQuery = true)
    void markMessageAsUnread(UUID messageId);

    @Modifying
    @Query(value = "UPDATE messages SET high_priority = true WHERE id = :messageId", nativeQuery = true)
    void markMessageAsHighPriority(UUID messageId);

    @Modifying
    @Query(value = "UPDATE messages SET high_priority = false WHERE id = :messageId", nativeQuery = true)
    void markMessageAsLowPriority(UUID messageId);

    @Modifying
    @Query(value = "UPDATE messages SET received = true WHERE id = :messageId", nativeQuery = true)
    void confirmMessageReceived(UUID messageId);

    @Modifying
    @Query(value = "UPDATE messages SET confirm_reading =  true WHERE id = :messageId", nativeQuery = true)
    void confirmMessageReading(UUID messageId);
}
