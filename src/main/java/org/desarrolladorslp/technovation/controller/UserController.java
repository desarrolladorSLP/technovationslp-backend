package org.desarrolladorslp.technovation.controller;

import java.util.List;

import org.desarrolladorslp.technovation.models.User;
import org.desarrolladorslp.technovation.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/user")
public class UserController {

    private UserService userService;

    @Secured("ROLE_ADMINISTRATOR")
    @GetMapping("/inactive")
    public ResponseEntity<List<User>> listInvalidUsers() {
        return new ResponseEntity<>(userService.findByValidated(false), HttpStatus.OK);
    }

    @Secured("ROLE_ADMINISTRATOR")
    @PostMapping("/activate")
    public ResponseEntity<User> activate(@RequestBody User user) {
        return new ResponseEntity<>(userService.activate(user), HttpStatus.OK);
    }

    @Secured("ROLE_ADMINISTRATOR")
    @GetMapping
    public ResponseEntity<List<User>> list() {
        return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
