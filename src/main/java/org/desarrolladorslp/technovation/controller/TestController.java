package org.desarrolladorslp.technovation.controller;

import java.security.Principal;

import org.desarrolladorslp.technovation.models.User;
import org.desarrolladorslp.technovation.services.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    private UserService userService;

    @GetMapping("/hello")
    public String helloWorldGet() {
        return "hello world!";
    }

    @PostMapping("/hello")
    public User helloWorldPost() {
        User user = userService.findByUsername((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        return user;
    }

    @PutMapping("/hello")
    public OAuth2Authentication helloWorldPost(OAuth2Authentication authentication) {

        return authentication;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
