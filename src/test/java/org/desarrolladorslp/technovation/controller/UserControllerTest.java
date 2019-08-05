package org.desarrolladorslp.technovation.controller;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.*;

import com.google.common.base.Verify;
import org.desarrolladorslp.technovation.TechnovationBackendApplication;
import org.desarrolladorslp.technovation.config.controller.LocalDateAdapter;
import org.desarrolladorslp.technovation.models.User;
import org.desarrolladorslp.technovation.repository.UserRepository;
import org.desarrolladorslp.technovation.services.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.util.NestedServletException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
@RunWith(Parameterized.class)
/*@RunWith(SpringRunner.class)
@SpringBootTest*/
@ContextConfiguration(classes = TechnovationBackendApplication.class)
@WebAppConfiguration
@AutoConfigureMockMvc
public class UserControllerTest {

    private static final String BASE_USER_URL = "/api/user";

    private MockMvc mockMvc;
    private Gson gson;

    @MockBean
    private UserService userService;
    @Autowired
    private WebApplicationContext webApplicationContext;

   private Authentication auth;

    public UserControllerTest(Authentication auth){
        this.auth = auth;
    }

    @Parameterized.Parameters
    public static Collection input()
    {
        return Arrays.asList(new Object[][]{
                {
                        new UsernamePasswordAuthenticationToken("user1@example.com", "user1",
                                Collections.singletonList(() -> "ROLE_ADMINISTRATOR"))
                },
                {
                        new UsernamePasswordAuthenticationToken("user1@example.com", "user1",
                                Collections.singletonList(() -> "ROLE_ROLE_TECKER"))
                }
        });
    }

    @Before
    public void setup(){
        gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateAdapter()).create();

        /*List<? extends GrantedAuthority> authorities = Collections.singletonList(() -> "ROLE_ADMINISTRATOR");
        Authentication auth = new UsernamePasswordAuthenticationToken("user1@example.com", "user1", authorities);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(auth);*/

        this.mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void whenListUsers_thenReturnListAnd200Status() throws Exception {

        //given
        Type UserListType = new TypeToken<ArrayList<User>>(){}.getType();
        String retrievedUsers = MessageLoader.loadExampleRequest("requests/user/list-existent-users.json");
        List<User> expectedList = gson.fromJson(retrievedUsers, UserListType);

       SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(auth);

        when(userService.findAll()).thenReturn(expectedList);

        //when
        try {
            MockHttpServletResponse response = mockMvc.perform(
                    MockMvcRequestBuilders.get(BASE_USER_URL)).andReturn().getResponse();

            //then
            assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
            verify(userService).findAll();
            verifyNoMoreInteractions(userService);

            String responseBody = response.getContentAsString();
            List<User> receivedUsers = gson.fromJson(responseBody, UserListType);
            assertThat(receivedUsers).isEqualTo(expectedList);

            fail("AccessDeniedException must be thrown");
        } catch (NestedServletException nestedServletException) {
            assertThat(nestedServletException.getCause()).isInstanceOf(AccessDeniedException.class);
        }
    }

    @Test
    public void whenListInvalidUsers_thenReturnListAnd200Status()throws Exception{
        //given
        Type listInvalidUsersType = new TypeToken<ArrayList<User>>(){}.getType();
        String retrievedUsers = MessageLoader.loadExampleRequest("requests/user/list-existent-users.json");
        List<User> expectedListInactive = gson.fromJson(retrievedUsers,listInvalidUsersType);

        when(userService.findByValidated(false)).thenReturn(expectedListInactive);

        //when
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.get(BASE_USER_URL + "/inactive")).andReturn().getResponse();

        //then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        verify(userService).findByValidated(false);
        verifyNoMoreInteractions(userService);

        String responseBody = response.getContentAsString();
        List<User> receivedUsersInactive = gson.fromJson(responseBody,listInvalidUsersType);
        assertThat(receivedUsersInactive).isEqualTo(expectedListInactive);
    }

    @Test
    public void givenAnExistentUserWithRoles_whenActivate_thenSuccessAnd200Status() throws Exception{
        //given
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

        String request = MessageLoader.loadExampleRequest("requests/user/valid-user.json");
        User user = gson.fromJson(request, User.class);

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

        assertThat(UserActivated.getId()).isEqualTo(user.getId());
        assertThat(UserActivated.getName()).isEqualTo(user.getName());
        assertThat(UserActivated.getPreferredEmail()).isEqualTo(user.getPreferredEmail());
        assertThat(UserActivated.isEnabled()).isEqualTo(user.isEnabled());
        assertThat(UserActivated.isValidated()).isTrue();
        assertThat(UserActivated.getRoles()).isEqualTo(user.getRoles());
    }

    @Test
    public void givenAnExistentUserWithEmptyRolesList_whenActivate_thenRejectWith400Status() throws Exception{
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
    public void givenAnUserWithIdNull_whenActivate_thenRejectWith404Status() throws Exception{

        //then
        //String request = MessageLoader.loadExampleRequest("requests/user/user-with-id-empty.json");
        String request = MessageLoader.loadExampleRequest("requests/user/user-with-id-null.json");
        User user = gson.fromJson(request, User.class);
        when(userService.activate(any(User.class))).thenThrow(new UsernameNotFoundException(""));

        //when
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.post(BASE_USER_URL + "/activate" )
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
        assertThat(userToActivate.getRoles()).isEqualTo(user.getRoles());
    }
}