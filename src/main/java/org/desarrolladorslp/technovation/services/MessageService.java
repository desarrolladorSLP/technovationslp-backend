package org.desarrolladorslp.technovation.services;

import org.desarrolladorslp.technovation.controller.dto.MessageHeaderDTO;
import org.desarrolladorslp.technovation.models.Message;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface MessageService {

    Map<String,List<MessageHeaderDTO>> getMessagesByUser(UUID userReceiverId);
}
