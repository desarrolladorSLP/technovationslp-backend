package org.desarrolladorslp.technovation.repository;

import org.desarrolladorslp.technovation.models.Message;
import org.desarrolladorslp.technovation.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID> {

    @Query("SELECT u FROM User u WHERE u.id IN (:userId)")
    List<User> getUsersExistent(List<UUID> userId);

    @Modifying
    @Query(value = "INSERT INTO messages_by_users(message_id,user_receiver_id) VALUES (:messageId, :userId)", nativeQuery = true)
    void messagesToUsers(UUID userId, UUID messageId);

    @Query(value = "SELECT * FROM messages AS m INNER JOIN messages_by_users AS mbu ON m.id = mbu.message_id WHERE mbu.user_receiver_id = :userReceiverId AND high_priority = :highPriority", nativeQuery = true)
    List<Message> getMessagesByPriority(UUID userReceiverId, Boolean highPriority);

}
