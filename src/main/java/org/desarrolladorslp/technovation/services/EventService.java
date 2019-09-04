package org.desarrolladorslp.technovation.services;

import org.desarrolladorslp.technovation.controller.dto.EventDTO;

import java.util.List;
import java.util.UUID;

public interface EventService {

    List<EventDTO> listEvents(int year, int month, UUID userId);
}
