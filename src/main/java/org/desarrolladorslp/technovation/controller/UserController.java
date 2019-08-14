package org.desarrolladorslp.technovation.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.desarrolladorslp.technovation.controller.dto.UserDTO;
import org.desarrolladorslp.technovation.models.Role;
import org.desarrolladorslp.technovation.models.User;
import org.desarrolladorslp.technovation.services.UserService;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
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

    private ModelMapper modelMapper;

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

    public User convertToEntity(UserDTO userDTO){

        Converter<UserDTO,  User> converter = mappingContext -> {

            User user = new User();
            List<Role> roles = new ArrayList<>();

            for (String s : mappingContext.getSource().getRoles()) {
                Role role = new Role();
                role.setName(s);
                roles.add(role);
            }

            user.setId(mappingContext.getSource().getId());
            user.setName(mappingContext.getSource().getName());
            user.setEnabled(mappingContext.getSource().isEnabled());
            user.setValidated(mappingContext.getSource().isValidated());
            user.setPreferredEmail(mappingContext.getSource().getPreferredEmail());
            user.setRoles(roles);

            return user;
        };

        if(modelMapper.getTypeMap(UserDTO.class, User.class) == null){
            modelMapper.createTypeMap(UserDTO.class, User.class).setConverter(converter);
        }

        return modelMapper.map(userDTO, User.class);
    }

    public UserDTO convertToDTO(User user){

        Converter<User, UserDTO> converter = mappingContext ->{

            UserDTO userDTO = new UserDTO();

            userDTO.setId(mappingContext.getSource().getId());
            userDTO.setName(mappingContext.getSource().getName());
            userDTO.setPreferredEmail(mappingContext.getSource().getPreferredEmail());
            userDTO.setEnabled(mappingContext.getSource().isEnabled());
            userDTO.setValidated(mappingContext.getSource().isValidated());
            userDTO.setRoles(mappingContext.getSource().getRoles().stream().map(Role::getName).collect(Collectors.toList()));

            return userDTO;

        };

        if(modelMapper.getTypeMap(User.class, UserDTO.class) == null) {
            modelMapper.createTypeMap(User.class, UserDTO.class).setConverter(converter);
        }

        return modelMapper.map(user, UserDTO.class);
    }

    @Autowired
    public void setModelMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }
}
