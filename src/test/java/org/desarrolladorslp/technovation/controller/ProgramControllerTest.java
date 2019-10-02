package org.desarrolladorslp.technovation.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.desarrolladorslp.technovation.config.controller.LocalDateAdapter;
import org.desarrolladorslp.technovation.models.Program;
import org.desarrolladorslp.technovation.services.ProgramService;
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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ProgramControllerTest {

    private static final String BASE_PROGRAM_URL = "/api/program";

    private MockMvc mockMvc;
    private Gson gson;

    @MockBean
    private ProgramService programService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setup() {
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();

        List<? extends GrantedAuthority> authorities = Collections.singletonList(() -> "ROLE_ADMINISTRATOR");
        Authentication auth = new UsernamePasswordAuthenticationToken("user1@example.com", "user1", authorities);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(auth);

        this.mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void givenANonAdministratorRole_whenCreateProgram_thenRejectAnd403Status() throws Exception {
        // given
        String request = MessageLoader.loadExampleRequest("requests/program/valid-program-with-id-null-01.json");

        List<? extends GrantedAuthority> authorities = Collections.singletonList(() -> "ROLE_TECKER");
        Authentication auth = new UsernamePasswordAuthenticationToken("user1@example.com", "user1", authorities);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(auth);

        // when
        try {
            mockMvc.perform(
                    MockMvcRequestBuilders.post(BASE_PROGRAM_URL)
                            .contentType(MediaType.APPLICATION_JSON_UTF8)
                            .content(request))
                    .andReturn().getResponse();
            fail("AccessDeniedException must be thrown");
        } catch (NestedServletException nestedServletException) {
            assertThat(nestedServletException.getCause()).isInstanceOf(AccessDeniedException.class);
        }
    }

    @Test
    public void givenValidProgram_whenCreateProgram_thenAcceptAnd201Status() throws Exception {
        // given
        ArgumentCaptor<Program> programCaptor = ArgumentCaptor.forClass(Program.class);

        String request = MessageLoader.loadExampleRequest("requests/program/valid-program-with-id-null-01.json");
        Program program = gson.fromJson(request, Program.class);

        // when
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.post(BASE_PROGRAM_URL)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(request))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        verify(programService).save(programCaptor.capture());
        verifyNoMoreInteractions(programService);

        Program programToInsert = programCaptor.getValue();
        assertThat(programToInsert.getId()).isNull();
        assertThat(programToInsert.getName()).isEqualTo(program.getName());
        assertThat(programToInsert.getDescription()).isEqualTo(program.getDescription());
        assertThat(programToInsert.getResponsible()).isEqualTo(program.getResponsible());
    }

    @Test
    public void givenValidProgramWithNonNullId_whenCreateProgram_thenAcceptAnd201Status() throws Exception {
        // given
        ArgumentCaptor<Program> programCaptor = ArgumentCaptor.forClass(Program.class);

        String request = MessageLoader.loadExampleRequest("requests/program/valid-program-with-non-null-id-01.json");
        Program program = gson.fromJson(request, Program.class);

        // when
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.post(BASE_PROGRAM_URL)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(request))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        verify(programService).save(programCaptor.capture());
        verifyNoMoreInteractions(programService);

        Program programToInsert = programCaptor.getValue();
        assertThat(programToInsert.getId()).isNull(); //Id is forced to be null
        assertThat(programToInsert.getName()).isEqualTo(program.getName());
        assertThat(programToInsert.getDescription()).isEqualTo(program.getDescription());
        assertThat(programToInsert.getResponsible()).isEqualTo(program.getResponsible());
    }

    @Test
    public void givenBadFormattedRequest_whenCreateProgram_thenRejectWith400Status() throws Exception {
        // given
        String request = MessageLoader.loadExampleRequest("requests/program/invalid-program-01.json");

        // when
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.post(BASE_PROGRAM_URL)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(request))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        verifyNoMoreInteractions(programService);

    }

    @Test
    public void givenValidProgramWithIdNull_whenUpdateProgram_thenRejectAnd400Status() throws Exception {
        // given
        String request = MessageLoader.loadExampleRequest("requests/program/valid-program-with-id-null-01.json");

        // when
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.put(BASE_PROGRAM_URL)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(request))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        verifyNoMoreInteractions(programService);
    }

    @Test
    public void givenValidProgramWithNonNullId_whenUpdateProgram_thenAcceptAnd200Status() throws Exception {
        // given
        ArgumentCaptor<Program> programCaptor = ArgumentCaptor.forClass(Program.class);

        String request = MessageLoader.loadExampleRequest("requests/program/valid-program-with-non-null-id-01.json");
        Program program = gson.fromJson(request, Program.class);

        // when
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.put(BASE_PROGRAM_URL)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(request))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        verify(programService).save(programCaptor.capture());
        verifyNoMoreInteractions(programService);

        Program programToInsert = programCaptor.getValue();
        assertThat(programToInsert.getId()).isEqualTo(program.getId());
        assertThat(programToInsert.getName()).isEqualTo(program.getName());
        assertThat(programToInsert.getDescription()).isEqualTo(program.getDescription());
        assertThat(programToInsert.getResponsible()).isEqualTo(program.getResponsible());
    }

    @Test
    public void givenInvalidProgram_whenUpdateProgram_thenRejectWith400Status() throws Exception {
        // given
        String request = MessageLoader.loadExampleRequest("requests/program/invalid-program-01.json");

        // when
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.put(BASE_PROGRAM_URL)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(request))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        verifyNoMoreInteractions(programService);
    }

    @Test
    public void whenListPrograms_thenReturnListAnd200Status() throws Exception {
        // given
        Type programListType = new TypeToken<ArrayList<Program>>() {
        }.getType();
        String retrievedPrograms = MessageLoader.loadExampleRequest("requests/program/list-existent-programs.json");
        List<Program> expectedList = gson.fromJson(retrievedPrograms, programListType);

        when(programService.list()).thenReturn(expectedList);

        // when
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.get(BASE_PROGRAM_URL))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        verify(programService).list();
        verifyNoMoreInteractions(programService);

        String responseBody = response.getContentAsString();
        List<Program> receivedPrograms = gson.fromJson(responseBody, programListType);
        assertThat(receivedPrograms).isEqualTo(expectedList);
    }

    @Test
    public void givenAnExistentProgramId_whenQueryProgramById_thenReturnProgramAnd200Status() throws Exception {
        // given
        String programToRetrieve = MessageLoader.loadExampleRequest("requests/program/valid-program-with-non-null-id-01.json");
        Program expectedProgram = gson.fromJson(programToRetrieve, Program.class);
        UUID programId = expectedProgram.getId();

        when(programService.findById(programId)).thenReturn(Optional.of(expectedProgram));

        // when
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.get(BASE_PROGRAM_URL + "/" + programId))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        verify(programService).findById(programId);
        verifyNoMoreInteractions(programService);

        String responseBody = response.getContentAsString();
        Program receivedProgram = gson.fromJson(responseBody, Program.class);
        assertThat(receivedProgram).isEqualTo(expectedProgram);
    }

    @Test
    public void givenAnNonExistentProgramId_whenQueryProgramById_then404Status() throws Exception {
        // given
        UUID programId = UUID.randomUUID();

        when(programService.findById(programId)).thenReturn(Optional.empty());

        // when
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.get(BASE_PROGRAM_URL + "/" + programId))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        verify(programService).findById(programId);
        verifyNoMoreInteractions(programService);
    }

    @Test
    public void givenAnExistentProgramId_whenDelete_then200Status() throws Exception {
        // given
        UUID programId = UUID.randomUUID();
        Optional<Program> program = Optional.of(Program.builder()
                .id(programId)
                .name(UUID.randomUUID().toString())
                .responsible(UUID.randomUUID().toString())
                .description(UUID.randomUUID().toString())
                .build());

        when(programService.delete(programId)).thenReturn(program);

        // when
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.delete(BASE_PROGRAM_URL + "/" + programId))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        verify(programService).delete(programId);
        verifyNoMoreInteractions(programService);
    }

    @Test
    public void givenANonExistentProgramId_whenDelete_then404Status() throws Exception {
        // given
        UUID programId = UUID.randomUUID();
        Optional<Program> program = Optional.empty();

        when(programService.delete(programId)).thenReturn(program);

        // when
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.delete(BASE_PROGRAM_URL + "/" + programId))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        verify(programService).delete(programId);
        verifyNoMoreInteractions(programService);
    }
}
