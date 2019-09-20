package org.desarrolladorslp.technovation.repository;

import org.desarrolladorslp.technovation.models.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID> {

    @Query(value = "SELECT * FROM messages AS m INNER JOIN messages_by_users AS mbu ON m.id = mbu.message_id WHERE mbu.user_receiver_id = :userReceiverId AND high_priority = :highPriority", nativeQuery = true)
    List<Message> getMessagesByPriority(UUID userReceiverId, Boolean highPriority);

    @Modifying
    @Query(value = "UPDATE messages SET unread = false WHERE id = :messageId", nativeQuery = true)
    void markMessageAsRead(UUID messageId);

}
