package org.desarrolladorslp.technovation.controller;

import static org.assertj.core.api.Assertions.assertThat;
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
import org.desarrolladorslp.technovation.models.Batch;
import org.desarrolladorslp.technovation.services.BatchService;
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
public class BatchControllerTest {

    private static final String BASE_BATCH_URL = "/api/batch";

    private MockMvc mockMvc;
    private Gson gson;

    @MockBean
    private BatchService batchService;

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
    public void givenValidBatch_whenCreateBatch_thenAcceptAnd201Status() throws Exception {
        // given
        ArgumentCaptor<Batch> peopleCaptor = ArgumentCaptor.forClass(Batch.class);

        String request = MessageLoader.loadExampleRequest("requests/batch/valid-batch-with-id-null-01.json");
        Batch batch = gson.fromJson(request, Batch.class);

        // when
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.post(BASE_BATCH_URL)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(request))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        verify(batchService).save(peopleCaptor.capture());
        verifyNoMoreInteractions(batchService);

        Batch batchToInsert = peopleCaptor.getValue();
        assertThat(batchToInsert.getId()).isNull();
        assertThat(batchToInsert.getEndDate()).isEqualTo(batch.getEndDate());
        assertThat(batchToInsert.getStartDate()).isEqualTo(batch.getStartDate());
        assertThat(batchToInsert.getName()).isEqualTo(batch.getName());
        assertThat(batchToInsert.getNotes()).isEqualTo(batch.getNotes());
        assertThat(batchToInsert.getProgram()).isNotNull();
        assertThat(batchToInsert.getProgram().getId()).isEqualTo(batch.getProgram().getId());
    }

    @Test
    public void givenValidBatchWithNonNullId_whenCreateBatch_thenAcceptAnd201Status() throws Exception {
        // given
        ArgumentCaptor<Batch> peopleCaptor = ArgumentCaptor.forClass(Batch.class);

        String request = MessageLoader.loadExampleRequest("requests/batch/valid-batch-with-non-null-id-01.json");
        Batch batch = gson.fromJson(request, Batch.class);

        // when
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.post(BASE_BATCH_URL)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(request))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        verify(batchService).save(peopleCaptor.capture());
        verifyNoMoreInteractions(batchService);

        Batch batchToInsert = peopleCaptor.getValue();
        assertThat(batchToInsert.getId()).isNull(); //Id is forced to be null
        assertThat(batchToInsert.getEndDate()).isEqualTo(batch.getEndDate());
        assertThat(batchToInsert.getStartDate()).isEqualTo(batch.getStartDate());
        assertThat(batchToInsert.getName()).isEqualTo(batch.getName());
        assertThat(batchToInsert.getNotes()).isEqualTo(batch.getNotes());
        assertThat(batchToInsert.getProgram()).isNotNull();
        assertThat(batchToInsert.getProgram().getId()).isEqualTo(batch.getProgram().getId());
    }

    @Test
    public void givenInvalidBatch_whenCreateBatch_thenRejectWith400Status() throws Exception {
        // given
        String request = MessageLoader.loadExampleRequest("requests/batch/invalid-batch-01.json");

        // when
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.post(BASE_BATCH_URL)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(request))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        verifyNoMoreInteractions(batchService);

    }

    @Test
    public void givenValidBatchWithIdNull_whenUpdateBatch_thenRejectAnd400Status() throws Exception {
        // given
        String request = MessageLoader.loadExampleRequest("requests/batch/valid-batch-with-id-null-01.json");

        // when
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.put(BASE_BATCH_URL)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(request))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        verifyNoMoreInteractions(batchService);
    }

    @Test
    public void givenValidBatchWithNonNullId_whenUpdateBatch_thenAcceptAnd200Status() throws Exception {
        // given
        ArgumentCaptor<Batch> peopleCaptor = ArgumentCaptor.forClass(Batch.class);

        String request = MessageLoader.loadExampleRequest("requests/batch/valid-batch-with-non-null-id-01.json");
        Batch batch = gson.fromJson(request, Batch.class);

        // when
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.put(BASE_BATCH_URL)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(request))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        verify(batchService).save(peopleCaptor.capture());
        verifyNoMoreInteractions(batchService);

        Batch batchToInsert = peopleCaptor.getValue();
        assertThat(batchToInsert.getId()).isEqualTo(batch.getId());
        assertThat(batchToInsert.getEndDate()).isEqualTo(batch.getEndDate());
        assertThat(batchToInsert.getStartDate()).isEqualTo(batch.getStartDate());
        assertThat(batchToInsert.getName()).isEqualTo(batch.getName());
        assertThat(batchToInsert.getNotes()).isEqualTo(batch.getNotes());
        assertThat(batchToInsert.getProgram()).isNotNull();
        assertThat(batchToInsert.getProgram().getId()).isEqualTo(batch.getProgram().getId());
    }

    @Test
    public void givenInvalidBatch_whenUpdateBatch_thenRejectWith400Status() throws Exception {
        // given
        String request = MessageLoader.loadExampleRequest("requests/batch/invalid-batch-01.json");

        // when
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.put(BASE_BATCH_URL)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(request))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        verifyNoMoreInteractions(batchService);
    }

    @Test
    public void whenListBatches_thenReturnListAnd200Status() throws Exception {
        // given
        Type batchListType = new TypeToken<ArrayList<Batch>>() {
        }.getType();
        String retrievedBatches = MessageLoader.loadExampleRequest("requests/batch/list-existent-batches.json");
        List<Batch> expectedList = gson.fromJson(retrievedBatches, batchListType);

        when(batchService.list()).thenReturn(expectedList);

        // when
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.get(BASE_BATCH_URL))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        verify(batchService).list();
        verifyNoMoreInteractions(batchService);

        String responseBody = response.getContentAsString();
        List<Batch> receivedBatches = gson.fromJson(responseBody, batchListType);
        assertThat(receivedBatches).isEqualTo(expectedList);
    }

    @Test
    public void givenAnExistentBatchId_whenQueryBatchById_thenReturnBatchAnd200Status() throws Exception {
        // given
        String batchToRetrieve = MessageLoader.loadExampleRequest("requests/batch/valid-batch-with-non-null-id-01.json");
        Batch expectedBatch = gson.fromJson(batchToRetrieve, Batch.class);
        UUID batchId = expectedBatch.getId();

        when(batchService.findById(batchId)).thenReturn(Optional.of(expectedBatch));

        // when
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.get(BASE_BATCH_URL + "/" + batchId))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        verify(batchService).findById(batchId);
        verifyNoMoreInteractions(batchService);

        String responseBody = response.getContentAsString();
        Batch receivedBatch = gson.fromJson(responseBody, Batch.class);
        assertThat(receivedBatch).isEqualTo(expectedBatch);
    }

    @Test
    public void givenAnNonExistentBatchId_whenQueryBatchById_then404Status() throws Exception {
        // given
        UUID batchId = UUID.randomUUID();

        when(batchService.findById(batchId)).thenReturn(Optional.empty());

        // when
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.get(BASE_BATCH_URL + "/" + batchId))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        verify(batchService).findById(batchId);
        verifyNoMoreInteractions(batchService);
    }

    @Test
    public void givenAnExistentProgramIdWithAssociatedBatches_whenListBatches_thenReturnListAnd200Status() throws Exception {
        // given
        Type batchListType = new TypeToken<ArrayList<Batch>>() {
        }.getType();
        String retrievedBatches = MessageLoader.loadExampleRequest("requests/batch/list-existent-batches.json");
        List<Batch> expectedList = gson.fromJson(retrievedBatches, batchListType);
        UUID programId = expectedList.get(0).getId();

        when(batchService.findByProgram(programId)).thenReturn(expectedList);

        // when
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.get(BASE_BATCH_URL + "/program/" + programId))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        verify(batchService).findByProgram(programId);
        verifyNoMoreInteractions(batchService);

        String responseBody = response.getContentAsString();
        List<Batch> receivedBatches = gson.fromJson(responseBody, batchListType);
        assertThat(receivedBatches).isEqualTo(expectedList);
    }

    @Test
    public void givenAnExistentProgramIdWithNoAssociatedBatches_whenListBatches_thenReturnEmptyListAnd200Status() throws Exception {
        // given
        Type batchListType = new TypeToken<ArrayList<Batch>>() {
        }.getType();
        List<Batch> expectedList = Collections.emptyList();
        UUID programId = UUID.randomUUID();

        when(batchService.findByProgram(programId)).thenReturn(expectedList);

        // when
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.get(BASE_BATCH_URL + "/program/" + programId))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        verify(batchService).findByProgram(programId);
        verifyNoMoreInteractions(batchService);

        String responseBody = response.getContentAsString();
        List<Batch> receivedBatches = gson.fromJson(responseBody, batchListType);
        assertThat(receivedBatches).isEqualTo(expectedList);
    }
}
