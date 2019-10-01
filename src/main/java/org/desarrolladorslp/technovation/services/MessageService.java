package org.desarrolladorslp.technovation.services;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.desarrolladorslp.technovation.dto.MessageBodyDTO;
import org.desarrolladorslp.technovation.dto.MessageHeaderDTO;

public interface MessageService {

    Map<String, List<MessageHeaderDTO>> getMessagesByUser(UUID userReceiverId);

    void markMessageAsRead(UUID messageId, UUID userReceiverId);

    void markMessageAsUnread(UUID messageId, UUID userReceiverId);

    MessageBodyDTO getSpecificMessageByUser(UUID messageId, UUID userReceiverId);

    void markMessageAsRead(UUID messageId);

    void markMessageAsHighPriority(UUID messageId, UUID userReceiverId);

    void markMessageAsLowPriority(UUID messageId, UUID userReceiverId);

    void confirmMessageReceived(UUID messageId, UUID userReceiverId);

    void confirmMessageReading(UUID messageId, UUID userReceiverId);
}
