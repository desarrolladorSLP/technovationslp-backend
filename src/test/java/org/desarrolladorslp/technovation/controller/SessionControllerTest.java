package org.desarrolladorslp.technovation.controller;

import com.google.common.base.Verify;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.desarrolladorslp.technovation.config.controller.LocalDateAdapter;
import org.desarrolladorslp.technovation.config.controller.LocalTimeAdapter;
import org.desarrolladorslp.technovation.models.Session;
import org.desarrolladorslp.technovation.services.SessionService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.util.NestedServletException;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class SessionControllerTest {

    private static final String BASE_SESSION_URL = "/api/session";

    private MockMvc mockMvc;
    private Gson gson;

    @MockBean
    private SessionService sessionService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setup() {
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .registerTypeAdapter(LocalTime.class, new LocalTimeAdapter())
                .create();

        List<? extends GrantedAuthority> authorities = Collections.singletonList(() -> "ROLE_ADMINISTRATOR");
        Authentication auth = new UsernamePasswordAuthenticationToken("user1@example.com", "user1", authorities);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(auth);

        this.mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void givenNonAdministratorRole_whenCreateSession_thenRejectAnd403Status() throws Exception {
        // given
        String request = MessageLoader.loadExampleRequest("requests/session/valid-session-with-id-null-01.json");

        List<? extends GrantedAuthority> authorities = Collections.singletonList(() -> "ROLE_TECKER");
        Authentication auth = new UsernamePasswordAuthenticationToken("user1@example.com", "user1", authorities);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(auth);

        // when
        try {
            mockMvc.perform(
                    MockMvcRequestBuilders.post(BASE_SESSION_URL)
                            .contentType(MediaType.APPLICATION_JSON_UTF8)
                            .content(request))
                    .andReturn().getResponse();
            fail("AccessDeniedException must be thrown");
        } catch (NestedServletException nestedServletException) {
            assertThat(nestedServletException.getCause()).isInstanceOf(AccessDeniedException.class);
        }
    }

    @Test
    public void givenValidSession_whenCreateSession_thenAcceptAnd201Status() throws Exception {
        // given
        ArgumentCaptor<Session> sessionCaptor = ArgumentCaptor.forClass(Session.class);

        String request = MessageLoader.loadExampleRequest("requests/session/valid-session-with-id-null-01.json");
        Session session = gson.fromJson(request, Session.class);

        // when
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.post(BASE_SESSION_URL)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(request))
                .andReturn().getResponse();

        //then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        verify(sessionService).save(sessionCaptor.capture());
        verifyNoMoreInteractions(sessionService);

        Session sessionToInsert = sessionCaptor.getValue();
        assertThat(sessionToInsert.getId()).isNull();
        assertThat(sessionToInsert.getDate()).isEqualTo(session.getDate());
        assertThat(sessionToInsert.getStartTime()).isEqualTo(session.getStartTime());
        assertThat(sessionToInsert.getEndTime()).isEqualTo(session.getEndTime());
        assertThat(sessionToInsert.getTitle()).isEqualTo(session.getTitle());
        assertThat(sessionToInsert.getNotes()).isEqualTo(session.getNotes());
        assertThat(sessionToInsert.getLocation()).isEqualTo(session.getLocation());
        assertThat(sessionToInsert.getBatch()).isNotNull();
        assertThat(sessionToInsert.getBatch().getId()).isEqualTo(session.getBatch().getId());


    }

    @Test
    public void givenValidSessionWithNonNullId_whenCreateSession_thenAcceptAnd201Status() throws Exception {
        // given
        ArgumentCaptor<Session> sessionCaptor = ArgumentCaptor.forClass(Session.class);

        String request = MessageLoader.loadExampleRequest("requests/session/valid-session-with-non-null-id-01.json");
        Session session = gson.fromJson(request, Session.class);

        // when
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.post(BASE_SESSION_URL)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(request))
                .andReturn().getResponse();

        //then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        verify(sessionService).save(sessionCaptor.capture());
        verifyNoMoreInteractions(sessionService);

        Session sessionToInsert = sessionCaptor.getValue();
        assertThat(sessionToInsert.getId()).isNull();
        assertThat(sessionToInsert.getDate()).isEqualTo(session.getDate());
        assertThat(sessionToInsert.getStartTime()).isEqualTo(session.getStartTime());
        assertThat(sessionToInsert.getEndTime()).isEqualTo(session.getEndTime());
        assertThat(sessionToInsert.getTitle()).isEqualTo(session.getTitle());
        assertThat(sessionToInsert.getNotes()).isEqualTo(session.getNotes());
        assertThat(sessionToInsert.getLocation()).isEqualTo(session.getLocation());
        assertThat(sessionToInsert.getBatch()).isNotNull();
        assertThat(sessionToInsert.getBatch().getId()).isEqualTo(session.getBatch().getId());


    }

    @Test
    public void givenBadFormattedRequest_whenCreateSession_thenRejectWith400Status() throws Exception {

        //given
        String request = MessageLoader.loadExampleRequest("requests/session/invalid-session-01.json");

        //when
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.post(BASE_SESSION_URL)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(request))
                .andReturn().getResponse();

        //then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        verifyNoMoreInteractions(sessionService);
    }

    @Test
    public void givenInvalidSession_whenCreateSession_thenRejectWith400Status() throws Exception {
        // given
        String request = MessageLoader.loadExampleRequest("requests/session/invalid-session-01.json");

        // when
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.post(BASE_SESSION_URL)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(request))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        verifyNoMoreInteractions(sessionService);
    }

    @Test
    public void givenValidSessionWithIdNull_whenUpdateSession_thenRejectAnd400Status() throws Exception {
        // given
        String request = MessageLoader.loadExampleRequest("requests/session/valid-session-with-id-null-01.json");

        // when
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.put(BASE_SESSION_URL)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(request))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        verifyNoMoreInteractions(sessionService);
    }

    @Test
    public void givenValidSessionWithNonNullId_whenUpdateSession_thenAcceptAnd200Status() throws Exception {
        // given
        ArgumentCaptor<Session> sessionCaptor = ArgumentCaptor.forClass(Session.class);

        String request = MessageLoader.loadExampleRequest("requests/session/valid-session-with-non-null-id-01.json");
        Session session = gson.fromJson(request, Session.class);

        // when
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.put(BASE_SESSION_URL)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(request))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        verify(sessionService).save((sessionCaptor.capture()));
        verifyNoMoreInteractions(sessionService);

        Session sessionToInsert = sessionCaptor.getValue();
        assertThat(sessionToInsert.getId()).isEqualTo(session.getId());
        assertThat(sessionToInsert.getDate()).isEqualTo(session.getDate());
        assertThat(sessionToInsert.getStartTime()).isEqualTo(session.getStartTime());
        assertThat(sessionToInsert.getEndTime()).isEqualTo(session.getEndTime());
        assertThat(sessionToInsert.getTitle()).isEqualTo(session.getTitle());
        assertThat(sessionToInsert.getNotes()).isEqualTo(session.getNotes());
        assertThat(sessionToInsert.getLocation()).isEqualTo(session.getLocation());
        assertThat(sessionToInsert.getBatch()).isNotNull();
        assertThat(sessionToInsert.getBatch().getId()).isEqualTo(session.getBatch().getId());

    }

    @Test
    public void givenInvalidSession_whenUpdateSession_thenRejectWith400Status() throws Exception {
        // given
        String request = MessageLoader.loadExampleRequest("requests/session/invalid-session-01.json");

        // when
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.put(BASE_SESSION_URL)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(request))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        verifyNoMoreInteractions(sessionService);
    }

    @Test
    public void whenListSessions_thenReturnListAnd200Status() throws Exception {
        // given
        Type sessionListType = new TypeToken<ArrayList<Session>>() {
        }.getType();
        String retrievedSessions = MessageLoader.loadExampleRequest("requests/session/list-existent-sessions.json");
        List<Session> expectedList = gson.fromJson(retrievedSessions, sessionListType);

        when(sessionService.list()).thenReturn(expectedList);

        // when
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.get(BASE_SESSION_URL))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        verify(sessionService).list();
        verifyNoMoreInteractions(sessionService);

        String responseBody = response.getContentAsString();
        List<Session> receivedSessions = gson.fromJson(responseBody, sessionListType);
        assertThat(receivedSessions).isEqualTo(expectedList);

    }

    @Test
    public void givenAnExistentSessionId_whenQuerySessionById_thenReturnBatchAnd200Status() throws Exception {

        // given
        String sessionToRetrieve = MessageLoader.loadExampleRequest("requests/session/valid-session-with-non-null-id-01.json");
        Session expectedSession = gson.fromJson(sessionToRetrieve, Session.class);
        UUID sessionId = expectedSession.getId();

        when(sessionService.findById(sessionId)).thenReturn(Optional.of(expectedSession));

        // when
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.get(BASE_SESSION_URL + "/" + sessionId))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        verify(sessionService).findById(sessionId);
        verifyNoMoreInteractions(sessionService);

        String responseBody = response.getContentAsString();
        Session receivedSession = gson.fromJson(responseBody, Session.class);
        assertThat(receivedSession).isEqualTo(expectedSession);

    }

    @Test
    public void givenANonExistentSessionId_whenQueryBatchById_then404Status() throws Exception {
        // given
        UUID sessionId = UUID.randomUUID();

        when(sessionService.findById(sessionId)).thenReturn(Optional.empty());

        // when
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.get(BASE_SESSION_URL + "/" + sessionId))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        verify(sessionService).findById(sessionId);
        verifyNoMoreInteractions(sessionService);

    }

    @Test
    public void givenAnExistentBatchIdWithAssociatedSessions_whenListSessions_thenReturnListAnd200Status() throws Exception {
        // given
        Type sessionListType = new TypeToken<ArrayList<Session>>() {
        }.getType();
        String retrievedSessions = MessageLoader.loadExampleRequest("requests/session/list-existent-sessions.json");
        List<Session> expectedList = gson.fromJson(retrievedSessions, sessionListType);
        UUID batchId = expectedList.get(0).getId();

        when(sessionService.findByBatch(batchId)).thenReturn(expectedList);

        // when
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.get(BASE_SESSION_URL + "/batch/" + batchId))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        verify(sessionService).findByBatch(batchId);

        String responseBody = response.getContentAsString();
        List<Session> receivedSessions = gson.fromJson(responseBody, sessionListType);
        assertThat(receivedSessions).isEqualTo(expectedList);
    }

    @Test
    public void givenAnExistentBatchIdWitNoAssociatedSessions_whenListSessions_thenReturnEmptyListAnd200Status() throws Exception {
        // given
        Type sessionListType = new TypeToken<ArrayList<Session>>() {
        }.getType();

        List<Session> expectedList = Collections.emptyList();
        UUID batchId = UUID.randomUUID();

        when(sessionService.findByBatch(batchId)).thenReturn(expectedList);

        // when
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.get(BASE_SESSION_URL + "/batch/" + batchId))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        verify(sessionService).findByBatch(batchId);
        verifyNoMoreInteractions(sessionService);
        String responseBody = response.getContentAsString();
        List<Session> receivedSessions = gson.fromJson(responseBody, sessionListType);
        assertThat(receivedSessions).isEqualTo(expectedList);

    }


}