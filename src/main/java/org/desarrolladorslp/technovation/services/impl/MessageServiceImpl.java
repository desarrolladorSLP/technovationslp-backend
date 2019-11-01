package org.desarrolladorslp.technovation.services.impl;

import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.desarrolladorslp.technovation.dto.MessageBodyDTO;
import org.desarrolladorslp.technovation.dto.MessageHeaderDTO;
import org.desarrolladorslp.technovation.dto.MessageReceiverDTO;
import org.desarrolladorslp.technovation.dto.ResourceDTO;
import org.desarrolladorslp.technovation.exception.MessageDoesNotBelongToUser;
import org.desarrolladorslp.technovation.models.Message;
import org.desarrolladorslp.technovation.models.Resource;
import org.desarrolladorslp.technovation.models.User;
import org.desarrolladorslp.technovation.repository.MessageRepository;
import org.desarrolladorslp.technovation.repository.ResourceRepository;
import org.desarrolladorslp.technovation.repository.UserRepository;
import org.desarrolladorslp.technovation.services.MessageService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MessageServiceImpl implements MessageService {

    private static final String HIGH_PRIORITY = "highPriority";
    private static final String LOW_PRIORITY = "lowPriority";
    private ModelMapper modelMapper;
    private MessageRepository messageRepository;
    private UserRepository userRepository;
    private ResourceRepository resourceRepository;

    @Override
    @Transactional(readOnly = true)
    public Map<String, List<MessageHeaderDTO>> getMessagesByUser(UUID userReceiverId) {
        Map<String, List<MessageHeaderDTO>> messages = new LinkedHashMap<>();
        messages.put(HIGH_PRIORITY, messageRepository.getMessagesByPriority(userReceiverId, true).stream().map(this::convertToDTO).collect(Collectors.toList()));
        messages.put(LOW_PRIORITY, messageRepository.getMessagesByPriority(userReceiverId, false).stream().map(this::convertToDTO).collect(Collectors.toList()));
        return messages;
    }

    @Override
    @Transactional(readOnly = true)
    public MessageBodyDTO getSpecificMessageByUser(UUID messageId, UUID userReceiverId) {
        Optional<Message> message = messageRepository.getBodyOfMessageByUser(messageId, userReceiverId);

        if (message.isPresent()) {
            List<MessageReceiverDTO> receivers = userRepository.getUsersReceiversToSpecificMessage(messageId).stream().map(this::convertUserReceiversToDTO).collect(Collectors.toList());
            List<ResourceDTO> attachments = resourceRepository.getResourcesBySpecificMessage(messageId).stream().map(this::convertResourceToDTO).collect(Collectors.toList());

            return createMessageDto(message.get(), receivers, attachments);
        }

        throw new NoSuchElementException();
    }

    @Override
    @Transactional
    public void markMessageAsRead(UUID messageId, UUID userReceiverId) {
        messageRepository.getBodyOfMessageByUser(messageId, userReceiverId).ifPresentOrElse(
                message -> messageRepository.markMessageAsRead(messageId),
                () -> {
                    throw new MessageDoesNotBelongToUser(messageId + "is not owned by user");
                }
        );
    }

    @Override
    @Transactional
    public void markMessageAsUnread(UUID messageId, UUID userReceiverId) {
        messageRepository.getBodyOfMessageByUser(messageId, userReceiverId).ifPresentOrElse(
                message -> messageRepository.markMessageAsUnread(messageId),
                () -> {
                    throw new MessageDoesNotBelongToUser(messageId + "is not owned by user");
                }
        );
    }

    @Override
    @Transactional
    public void markMessageAsHighPriority(UUID messageId, UUID userReceiverId) {
        messageRepository.getBodyOfMessageByUser(messageId, userReceiverId).ifPresentOrElse(
                message -> messageRepository.markMessageAsHighPriority(messageId),
                () -> {
                    throw new MessageDoesNotBelongToUser(messageId + "is not owned by user");
                }
        );
    }

    @Override
    @Transactional
    public void markMessageAsLowPriority(UUID messageId, UUID userReceiverId) {
        messageRepository.getBodyOfMessageByUser(messageId, userReceiverId).ifPresentOrElse(
                message -> messageRepository.markMessageAsLowPriority(messageId),
                () -> {
                    throw new MessageDoesNotBelongToUser(messageId + "is not owned by user");
                }
        );
    }

    @Override
    @Transactional
    public void confirmMessageReceived(UUID messageId, UUID userReceiverId) {
        messageRepository.getBodyOfMessageByUser(messageId, userReceiverId).ifPresentOrElse(
                message -> messageRepository.confirmMessageReceived(messageId),
                () -> {
                    throw new MessageDoesNotBelongToUser(messageId + "is not owned by user");
                }
        );
    }

    @Override
    @Transactional
    public void confirmMessageReading(UUID messageId, UUID userReceiverId) {
        messageRepository.getBodyOfMessageByUser(messageId, userReceiverId).ifPresentOrElse(
                message -> messageRepository.confirmMessageReading(messageId),
                () -> {
                    throw new MessageDoesNotBelongToUser(messageId + "is not owned by user");
                }
        );
    }

    @Autowired
    public void setModelMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setResourceRepository(ResourceRepository resourceRepository) {
        this.resourceRepository = resourceRepository;
    }

    @Autowired
    public void setMessageRepository(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    private ResourceDTO convertResourceToDTO(Resource resource) {
        return modelMapper.map(resource, ResourceDTO.class);
    }

    private MessageReceiverDTO convertUserReceiversToDTO(User user) {

        return modelMapper.map(user, MessageReceiverDTO.class);
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

    private MessageBodyDTO createMessageDto(Message message, List<MessageReceiverDTO> receivers, List<ResourceDTO> attachments) {
        return MessageBodyDTO.builder()
                .body(message.getBody())
                .subject(message.getTitle())
                .messageId(message.getId())
                .senderId(message.getUserSender().getId())
                .senderName(message.getUserSender().getName())
                .senderImage(message.getUserSender().getPictureUrl())
                .timestamp(message.getDateTime().format(DateTimeFormatter.ISO_INSTANT))
                .receivers(receivers)
                .attachments(attachments)
                .build();
    }
}
