package org.desarrolladorslp.technovation.services;

import org.desarrolladorslp.technovation.models.Message;

import java.util.List;
import java.util.UUID;

public interface MessageService {

    Message sendMessage(Message message, UUID userId, List<String> usersId);
}
