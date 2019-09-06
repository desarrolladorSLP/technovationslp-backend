package org.desarrolladorslp.technovation.controller;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import org.desarrolladorslp.technovation.config.auth.TokenInfoService;
import org.desarrolladorslp.technovation.models.Message;
import org.desarrolladorslp.technovation.models.User;
import org.desarrolladorslp.technovation.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Type;
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
        Message message = new Message(null,obj.get("title").toString(),obj.get("body").toString(),null,null);
        messageService.sendMessage(message,tokenInfoService.getIdFromPrincipal(principal), usersId);
        return new ResponseEntity<>( HttpStatus.OK);
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
