package org.desarrolladorslp.technovation.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.desarrolladorslp.technovation.config.controller.LocalDateAdapter;
import org.desarrolladorslp.technovation.dto.UserDTO;
import org.desarrolladorslp.technovation.dto.UsersByRoleDTO;
import org.desarrolladorslp.technovation.models.Role;
import org.desarrolladorslp.technovation.models.User;
import org.desarrolladorslp.technovation.services.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    private static final String BASE_USER_URL = "/api/user";

    private MockMvc mockMvc;
    private Gson gson;

    @MockBean
    private UserService userService;
    @Autowired
    private WebApplicationContext webApplicationContext;


    @Before
    public void setup() {
        gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateAdapter()).create();

        List<? extends GrantedAuthority> authorities = Collections.singletonList(() -> "ROLE_ADMINISTRATOR");
        Authentication auth = new UsernamePasswordAuthenticationToken("user1@example.com", "user1", authorities);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(auth);

        this.mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void whenListUsers_thenReturnListAnd200Status() throws Exception {

        //given
        Type UserDTOListType = new TypeToken<ArrayList<UserDTO>>() {
        }.getType();
        String retrievedUsersDTO = MessageLoader.loadExampleRequest("requests/user/list-existent-usersDTO.json");
        List<UserDTO> expectedListDTO = gson.fromJson(retrievedUsersDTO, UserDTOListType);

        Type UserListType = new TypeToken<ArrayList<User>>() {
        }.getType();
        String retrievedUsers = MessageLoader.loadExampleRequest("requests/user/list-existent-users.json");
        List<User> expectedList = gson.fromJson(retrievedUsers, UserListType);

        when(userService.findAll()).thenReturn(expectedList);

        //when
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.get(BASE_USER_URL)).andReturn().getResponse();

        //then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        verify(userService).findAll();
        verifyNoMoreInteractions(userService);

        String responseBody = response.getContentAsString();
        List<UserDTO> receivedUsersDTO = gson.fromJson(responseBody, UserDTOListType);
        assertThat(receivedUsersDTO).isEqualTo(expectedListDTO);
    }

    @Test
    public void whenListActiveUsers_thenReturnListAnd200Status() throws Exception {
        //given
        Type listActiveDTOListType = new TypeToken<ArrayList<UserDTO>>() {
        }.getType();
        String retrievedUsersDTO = MessageLoader.loadExampleRequest("requests/user/list-existent-usersDTO.json");
        List<UserDTO> expectedListActiveDTO = gson.fromJson(retrievedUsersDTO, listActiveDTOListType);

        Type listActiveUsersType = new TypeToken<ArrayList<User>>() {
        }.getType();
        String retrievedUsers = MessageLoader.loadExampleRequest("requests/user/list-existent-users.json");
        List<User> expectedListActive = gson.fromJson(retrievedUsers, listActiveUsersType);

        when(userService.findByValidated(true)).thenReturn(expectedListActive);

        //when
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.get(BASE_USER_URL + "/active")).andReturn().getResponse();

        //then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        verify(userService).findByValidated(true);
        verifyNoMoreInteractions(userService);

        String responseBody = response.getContentAsString();
        List<UserDTO> receivedUsersActiveDTO = gson.fromJson(responseBody, listActiveDTOListType);
        assertThat(receivedUsersActiveDTO).isEqualTo(expectedListActiveDTO);
    }

    @Test
    public void whenListInactiveUsers_thenReturnListAnd200Status() throws Exception {
        //given
        Type listInactiveDTOListType = new TypeToken<ArrayList<UserDTO>>() {
        }.getType();
        String retrievedUsersDTO = MessageLoader.loadExampleRequest("requests/user/list-existent-usersDTO.json");
        List<UserDTO> expectedListInactiveDTO = gson.fromJson(retrievedUsersDTO, listInactiveDTOListType);

        Type listInactiveUsersType = new TypeToken<ArrayList<User>>() {
        }.getType();
        String retrievedUsers = MessageLoader.loadExampleRequest("requests/user/list-existent-users.json");
        List<User> expectedListInactive = gson.fromJson(retrievedUsers, listInactiveUsersType);

        when(userService.findByValidated(false)).thenReturn(expectedListInactive);

        //when
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.get(BASE_USER_URL + "/inactive")).andReturn().getResponse();

        //then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        verify(userService).findByValidated(false);
        verifyNoMoreInteractions(userService);

        String responseBody = response.getContentAsString();
        List<UserDTO> receivedUsersInactiveDTO = gson.fromJson(responseBody, listInactiveDTOListType);
        assertThat(receivedUsersInactiveDTO).isEqualTo(expectedListInactiveDTO);
    }

    @Test
    public void givenAnExistentUserWithRoles_whenActivate_thenSuccessAnd200Status() throws Exception {
        //given
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

        String request = MessageLoader.loadExampleRequest("requests/user/valid-user-DTOs.json");
        UserDTO userDTO = gson.fromJson(request, UserDTO.class);

        String requestUser = MessageLoader.loadExampleRequest("requests/user/valid-user.json");
        User user = gson.fromJson(requestUser, User.class);

        assertThat(userDTO.getId()).isNotNull();
        assertThat(user.getId()).isEqualTo(userDTO.getId());
        assertThat(user.getPreferredEmail()).isEqualTo(userDTO.getPreferredEmail());
        assertThat(user.isEnabled()).isEqualTo(userDTO.isEnabled());
        assertThat(user.isValidated()).isTrue();
        assertThat(user.getRoles()).isEqualTo(userDTO.getRoles().stream().map(roleName -> Role.builder().name(roleName).build()).collect(Collectors.toList()));

        //when
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.post(BASE_USER_URL + "/activate")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(request))
                .andReturn().getResponse();

        //then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        verify(userService).activate(userCaptor.capture());
        verifyNoMoreInteractions(userService);

        User UserActivated = userCaptor.getValue();
        assertThat(UserActivated).isNotNull();

        assertThat(UserActivated.getId()).isEqualTo(userDTO.getId());
        assertThat(UserActivated.getName()).isEqualTo(userDTO.getName());
        assertThat(UserActivated.getPreferredEmail()).isEqualTo(userDTO.getPreferredEmail());
        assertThat(UserActivated.isEnabled()).isEqualTo(userDTO.isEnabled());
        assertThat(UserActivated.isValidated()).isTrue();
        assertThat(UserActivated.getRoles()).isEqualTo(userDTO.getRoles().stream().map(roleName -> Role.builder().name(roleName).build()).collect(Collectors.toList()));
    }

    @Test
    public void givenAnExistentUserWithEmptyRolesList_whenActivate_thenRejectWith400Status() throws Exception {
        //given
        String request = MessageLoader.loadExampleRequest("requests/user/user-with-empty-roles-list.json");
        when(userService.activate(any(User.class))).thenThrow(new IllegalArgumentException());
        User user = gson.fromJson(request, User.class);

        //when
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.post(BASE_USER_URL + "/activate")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(request))
                .andReturn().getResponse();

        //then
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userService).activate(userCaptor.capture());
        verifyNoMoreInteractions(userService);
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());

        User userToActivate = userCaptor.getValue();
        assertThat(userToActivate).isNotNull();
        assertThat(userToActivate.getId()).isEqualTo(user.getId());
        assertThat(userToActivate.getRoles()).isNullOrEmpty();

    }

    @Test
    public void givenAnUserWithIdNull_whenActivate_thenRejectWith404Status() throws Exception {

        //given
        String request = MessageLoader.loadExampleRequest("requests/user/user-with-id-null-DTOs.json");
        UserDTO userDTO = gson.fromJson(request, UserDTO.class);
        when(userService.activate(any(User.class))).thenThrow(new UsernameNotFoundException(""));


        //when
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.post(BASE_USER_URL + "/activate")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(request))
                .andReturn().getResponse();

        //then
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userService).activate(userCaptor.capture());
        verifyNoMoreInteractions(userService);
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());


        User userToActivate = userCaptor.getValue();
        assertThat(userToActivate).isNotNull();
        assertThat(userToActivate.getId()).isNull();
        assertThat(userToActivate.getRoles()).isEqualTo(userDTO.getRoles().stream().map(roleName -> Role.builder().name(roleName).build()).collect(Collectors.toList()));
    }

    @Test
    public void whenGetUsersByRole_thenSuccessAnd200Status() throws Exception{

        //given
        Type usersByRoleListType = new TypeToken<ArrayList<UsersByRoleDTO>>() {
        }.getType();
        String request = MessageLoader.loadExampleRequest("requests/user/usersDTO-with-role.json");
        ArrayList<UsersByRoleDTO>expectedListUsersByRole = gson.fromJson(request, usersByRoleListType);
        String role = "ROLE_ADMINISTRATOR";
        when(userService.getUsersByRole(role)).thenReturn(expectedListUsersByRole);

        //when
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.get(BASE_USER_URL + "/role/"+ role)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(request))
                .andReturn().getResponse();

        //then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        verify(userService).getUsersByRole(role);
        verifyNoMoreInteractions(userService);

        String responseBody = response.getContentAsString();
        List<UsersByRoleDTO> receivedUsersByRoleDTO = gson.fromJson(responseBody, usersByRoleListType);
        assertThat(receivedUsersByRoleDTO).isEqualTo(expectedListUsersByRole);


    }
}
