package org.desarrolladorslp.technovation.controller;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.desarrolladorslp.technovation.config.auth.TokenInfoService;
import org.desarrolladorslp.technovation.dto.MessageHeaderDTO;
import org.desarrolladorslp.technovation.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/message")
public class MessageController {

    private MessageService messageService;

    private TokenInfoService tokenInfoService;

    @GetMapping
    public ResponseEntity<Map<String, List<MessageHeaderDTO>>> retrieveMessage(Principal principal) {
        Map<String, List<MessageHeaderDTO>> messages = messageService.getMessagesByUser(tokenInfoService.getIdFromPrincipal(principal));

        return new ResponseEntity<>(messages, HttpStatus.OK);
    }

    @PutMapping("/{messageId}/read")
    public ResponseEntity markMessageAsRead(@PathVariable UUID messageId){

        messageService.markMessageAsRead(messageId);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PutMapping("/{messageId}/unread")
    public ResponseEntity markMessageAsUnread(@PathVariable UUID messageId){
        messageService.markMessageAsUnread(messageId);
        return new ResponseEntity(HttpStatus.OK);
    }

    @Autowired
    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }

    @Autowired
    public void setTokenInfoService(TokenInfoService tokenInfoService) {
        this.tokenInfoService = tokenInfoService;
    }
}
