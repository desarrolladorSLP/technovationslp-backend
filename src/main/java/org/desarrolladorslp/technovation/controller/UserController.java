package org.desarrolladorslp.technovation.controller;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

import org.desarrolladorslp.technovation.config.auth.TokenInfoService;
import org.desarrolladorslp.technovation.dto.UserDTO;
import org.desarrolladorslp.technovation.dto.UserPictureDTO;
import org.desarrolladorslp.technovation.dto.UsersByRoleDTO;
import org.desarrolladorslp.technovation.models.Role;
import org.desarrolladorslp.technovation.models.User;
import org.desarrolladorslp.technovation.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/user")
public class UserController {

    private UserService userService;

    private TokenInfoService tokenInfoService;

    @Secured("ROLE_ADMINISTRATOR")
    @GetMapping("/inactive")
    public ResponseEntity<List<UserDTO>> listInactiveUsers() {

        List<User> users = userService.findByValidated(false);

        return new ResponseEntity<>(users.stream().map(this::convertToDTO).collect(Collectors.toList()), HttpStatus.OK);
    }

    @Secured("ROLE_ADMINISTRATOR")
    @GetMapping("/active")
    public ResponseEntity<List<UserDTO>> listActiveUsers() {

        List<User> users = userService.findByValidated(true);

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

    @GetMapping("/me")
    public ResponseEntity<UserDTO> getProfileInfo(Principal principal) {
        User user = userService.findById(tokenInfoService.getIdFromPrincipal(principal));

        return new ResponseEntity<>(convertToDTO(user), HttpStatus.OK);
    }

    @PostMapping("/me")
    public ResponseEntity<UserDTO> updateProfileInfo(@RequestBody UserDTO userDTO, Principal principal) {
        User user = userService.findById(tokenInfoService.getIdFromPrincipal(principal));

        user.setName(userDTO.getName());
        user.setPreferredEmail(userDTO.getPreferredEmail());
        user.setPhoneNumber(userDTO.getPhoneNumber());

        return new ResponseEntity<>(convertToDTO(userService.save(user)), HttpStatus.OK);
    }

    @GetMapping("/role/{roleName}")
    public ResponseEntity<List<UsersByRoleDTO>> getUsersByRole(@PathVariable String roleName) {
        return new ResponseEntity<>(userService.getUsersByRole(roleName), HttpStatus.OK);
    }

    @PutMapping("picture")
    public ResponseEntity<HttpStatus> updatePicture(@RequestBody UserPictureDTO picture, Principal principal){
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private User convertToEntity(UserDTO userDTO) {

        return User.builder()
                .id(userDTO.getId())
                .name(userDTO.getName())
                .phoneNumber(userDTO.getPhoneNumber())
                .pictureUrl(userDTO.getPictureUrl())
                .enabled(userDTO.isEnabled())
                .validated(userDTO.isValidated())
                .preferredEmail(userDTO.getPreferredEmail())
                .roles(userDTO.getRoles().stream().map(roleName -> Role.builder().name(roleName).build()).collect(Collectors.toList()))
                .build();

    }

    private UserDTO convertToDTO(User user) {

        return UserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .phoneNumber(user.getPhoneNumber())
                .pictureUrl(user.getPictureUrl())
                .enabled(user.isEnabled())
                .validated(user.isValidated())
                .preferredEmail(user.getPreferredEmail())
                .roles(user.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
                .build();
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setTokenInfoService(TokenInfoService tokenInfoService) {
        this.tokenInfoService = tokenInfoService;
    }
}
