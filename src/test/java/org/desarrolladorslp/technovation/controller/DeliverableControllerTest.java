package org.desarrolladorslp.technovation.controller;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.desarrolladorslp.technovation.config.controller.LocalDateAdapter;
import org.desarrolladorslp.technovation.dto.DeliverableDTO;
import org.desarrolladorslp.technovation.models.Deliverable;
import org.desarrolladorslp.technovation.services.DeliverableService;
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
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class DeliverableControllerTest {

    private static final String BASE_DELIVERABLE_URL = "/api/deliverable";

    private MockMvc mockMvc;
    private Gson gson;

    @MockBean
    private DeliverableService deliverableService;

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
    public void givenValidDeliverable_whenCreateDeliverable_thenAcceptAnd201Status() throws Exception {
        //given
        ArgumentCaptor<DeliverableDTO> deliverableDTOCaptor = ArgumentCaptor.forClass(DeliverableDTO.class);

        String request = MessageLoader.loadExampleRequest("requests/deliverable/valid-deliverable.json");
        DeliverableDTO deliverableDTO = gson.fromJson(request, DeliverableDTO.class);

        // when
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.post(BASE_DELIVERABLE_URL)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(request))
                .andReturn().getResponse();

        //then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        verify(deliverableService).save(deliverableDTOCaptor.capture());
        verifyNoMoreInteractions(deliverableService);

        DeliverableDTO deliverableToInsert = deliverableDTOCaptor.getValue();
        assertThat(deliverableToInsert).isEqualTo(deliverableDTO);
    }

    @Test
    public void whenListDeliverablesByBatchId_thenReturnListAnd200Status() throws Exception {
        //given
        Type deliverableDTOListType = new TypeToken<ArrayList<DeliverableDTO>>() {
        }.getType();
        String retrievedDeliverablesDTO = MessageLoader.loadExampleRequest("requests/deliverable/list-deliverablesDTO.json");
        List<DeliverableDTO> expectedListDTO = gson.fromJson(retrievedDeliverablesDTO, deliverableDTOListType);

        UUID batchId = UUID.randomUUID();

        when(deliverableService.findByBatch(batchId)).thenReturn(expectedListDTO);

        // when
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.get(BASE_DELIVERABLE_URL + "/batch/" + batchId))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        verify(deliverableService).findByBatch(batchId);
        verifyNoMoreInteractions(deliverableService);

        String responseBody = response.getContentAsString();
        List<DeliverableDTO> receivedDeliverablesDTO = gson.fromJson(responseBody, deliverableDTOListType);
        assertThat(receivedDeliverablesDTO).isEqualTo(expectedListDTO);

    }

    @Test
    public void givenValidDeliverable_whenUpdateDeliverable_thenAcceptAnd200Status() throws Exception {
        //given
        ArgumentCaptor<DeliverableDTO> deliverableDTOCaptor = ArgumentCaptor.forClass(DeliverableDTO.class);
        ArgumentCaptor<UUID> idCaptor  = ArgumentCaptor.forClass(UUID.class);

        String request = MessageLoader.loadExampleRequest("requests/deliverable/valid-deliverable.json");
        DeliverableDTO deliverableDTO = gson.fromJson(request, DeliverableDTO.class);
        UUID deliverableId = UUID.randomUUID();

        when(deliverableService.update(deliverableDTO, deliverableId)).thenReturn(new DeliverableDTO());

        // when
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.put(BASE_DELIVERABLE_URL +"/" + deliverableId)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(request))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        verify(deliverableService).update(deliverableDTOCaptor.capture(), idCaptor.capture());
        verifyNoMoreInteractions(deliverableService);

        DeliverableDTO deliverableToInsert = deliverableDTOCaptor.getValue();
        assertThat(deliverableToInsert).isEqualTo(deliverableDTO);
    }

    @Test
    public void givenAnExistentDeliverableId_whenDelete_then200Status() throws Exception {
        // given
        UUID deliverableId = UUID.randomUUID();

        // when
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.delete(BASE_DELIVERABLE_URL + "/" + deliverableId))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        verify(deliverableService).delete(deliverableId);
        verifyNoMoreInteractions(deliverableService);
    }
}
