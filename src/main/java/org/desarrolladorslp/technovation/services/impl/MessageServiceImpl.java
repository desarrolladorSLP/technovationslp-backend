package org.desarrolladorslp.technovation.services.impl;

import org.desarrolladorslp.technovation.models.Message;
import org.desarrolladorslp.technovation.models.User;
import org.desarrolladorslp.technovation.repository.MessageRepository;
import org.desarrolladorslp.technovation.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.crypto.Data;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class MessageServiceImpl implements MessageService {

    private MessageRepository messageRepository;

    @Override
    @Transactional
    public Message sendMessage(Message message, UUID userId, List<String> users){

        List<UUID> usersId = new ArrayList<>();
        for (String id: users) {
            usersId.add(UUID.fromString(id));
        }
        List<User> usersExistent =  messageRepository.getUsersExistent(usersId);

        /*LocalDateTime date = LocalDateTime.now(ZoneOffset.UTC);
        System.out.println(date);*/

        Instant instant = new Date().toInstant();
        LocalDateTime localDateTime = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();

        if(Objects.isNull(message.getId())){
            message.setId(UUID.randomUUID());
        }
        message.setUserSender(userId);
        message.setDateTime(localDateTime);

        for(User user: usersExistent){
            messageRepository.messagesToUsers(user.getId(),message.getId());
        }

        return messageRepository.save(message);
    }

    @Autowired
    public void setMessageRepository(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }
}
