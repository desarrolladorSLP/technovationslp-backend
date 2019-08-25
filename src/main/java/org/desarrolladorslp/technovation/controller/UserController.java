package org.desarrolladorslp.technovation.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.desarrolladorslp.technovation.controller.dto.UserDTO;
import org.desarrolladorslp.technovation.models.Role;
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
    public ResponseEntity<List<UserDTO>> listInvalidUsers() {

        List<User> users = userService.findByValidated(false);

        return new ResponseEntity<>(users.stream().map(this::convertToDTO).collect(Collectors.toList()), HttpStatus.OK);
    }

    @Secured("ROLE_ADMINISTRATOR")
    @PostMapping("/activate")
    public ResponseEntity<Void> activate(@RequestBody UserDTO userDTO) {

        User user = convertToEntity(userDTO);

        userService.activate(user);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Secured("ROLE_ADMINISTRATOR")
    @GetMapping
    public ResponseEntity<List<UserDTO>> list() {

        List<User> users = userService.findAll();

        List<UserDTO> userDTOS = users.stream().map(this::convertToDTO).collect(Collectors.toList());
        return new ResponseEntity<>(userDTOS, HttpStatus.OK);
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public User convertToEntity(UserDTO userDTO) {

        User user = new User();

        user.setId(userDTO.getId());
        user.setName(userDTO.getName());
        user.setEnabled(userDTO.isEnabled());
        user.setValidated(userDTO.isValidated());
        user.setPreferredEmail(userDTO.getPreferredEmail());
        user.setRoles(userDTO.getRoles().stream().map(roleName -> Role.builder().name(roleName).build()).collect(Collectors.toList()));

        return user;

    }

    public UserDTO convertToDTO(User user) {

        UserDTO userDTO = new UserDTO();

        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setPreferredEmail(user.getPreferredEmail());
        userDTO.setEnabled(user.isEnabled());
        userDTO.setValidated(user.isValidated());
        userDTO.setRoles(user.getRoles().stream().map(Role::getName).collect(Collectors.toList()));

        return userDTO;
    }

}
