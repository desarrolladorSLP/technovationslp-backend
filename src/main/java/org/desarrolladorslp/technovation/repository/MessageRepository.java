package org.desarrolladorslp.technovation.repository;

import org.desarrolladorslp.technovation.dto.ResourceDTO;
import org.desarrolladorslp.technovation.models.Message;
import org.desarrolladorslp.technovation.models.Resource;
import org.desarrolladorslp.technovation.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID> {

    @Query(value = "SELECT * FROM messages AS m INNER JOIN messages_by_users AS mbu ON m.id = mbu.message_id WHERE mbu.user_receiver_id = :userReceiverId AND high_priority = :highPriority", nativeQuery = true)
    List<Message> getMessagesByPriority(UUID userReceiverId, Boolean highPriority);

    @Query("SELECT m FROM Message m WHERE m.id = :messageId")
    Message getBodyOfMessageByUser(UUID messageId);
}
