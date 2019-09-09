package org.desarrolladorslp.technovation.controller;

import com.google.gson.internal.LinkedTreeMap;
import org.desarrolladorslp.technovation.config.auth.TokenInfoService;
import org.desarrolladorslp.technovation.controller.dto.MessageHeaderDTO;
import org.desarrolladorslp.technovation.models.Message;
import org.desarrolladorslp.technovation.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.*;

@RestController
@RequestMapping("api/message")
public class MessageController {


    private MessageService messageService;

    private TokenInfoService tokenInfoService;


    @RequestMapping("/send")
    @PostMapping
    public ResponseEntity<Message> sendMessage(@RequestBody LinkedTreeMap obj, Principal principal){
        List<String> usersId = (List<String>) obj.get("users");
        Message message = new Message(null,obj.get("title").toString(),obj.get("body").toString(),(boolean)obj.get("highPriority"),null,null);
        messageService.sendMessage(message,tokenInfoService.getIdFromPrincipal(principal), usersId);
        return new ResponseEntity<>( HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Map<String,List<MessageHeaderDTO>>> retrieveMessage(Principal principal){
        Map<String,List<MessageHeaderDTO>> messages = messageService.getMessagesByUser(tokenInfoService.getIdFromPrincipal(principal));

        return new ResponseEntity<>(messages,HttpStatus.OK);
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
