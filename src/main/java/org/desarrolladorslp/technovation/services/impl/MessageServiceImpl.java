package org.desarrolladorslp.technovation.services.impl;

import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.desarrolladorslp.technovation.dto.MessageHeaderDTO;
import org.desarrolladorslp.technovation.models.Message;
import org.desarrolladorslp.technovation.repository.MessageRepository;
import org.desarrolladorslp.technovation.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MessageServiceImpl implements MessageService {


    private static final String HIGH_PRIORITY = "highPriority";
    private static final String LOW_PRIORITY = "lowPriority";
    private MessageRepository messageRepository;

    @Override
    @Transactional(readOnly = true)
    public Map<String, List<MessageHeaderDTO>> getMessagesByUser(UUID userReceiverId) {
        Map<String, List<MessageHeaderDTO>> messages = new LinkedHashMap<>();
        messages.put(HIGH_PRIORITY, messageRepository.getMessagesByPriority(userReceiverId, true).stream().map(this::convertToDTO).collect(Collectors.toList()));
        messages.put(LOW_PRIORITY, messageRepository.getMessagesByPriority(userReceiverId, false).stream().map(this::convertToDTO).collect(Collectors.toList()));
        return messages;
    }

    @Autowired
    public void setMessageRepository(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    private MessageHeaderDTO convertToDTO(Message message) {
        return MessageHeaderDTO.builder()
                .messageId(message.getId())
                .sender(message.getUserSender().getName())
                .senderImage(message.getUserSender().getPictureUrl())
                .subject(message.getTitle())
                .timestamp(message.getDateTime().format(DateTimeFormatter.ISO_INSTANT))
                .userSenderId(message.getUserSender().getId())
                .build();
    }
}
