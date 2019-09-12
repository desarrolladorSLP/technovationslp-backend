package org.desarrolladorslp.technovation.services;

import java.util.List;
import java.util.UUID;

import org.desarrolladorslp.technovation.dto.EventDTO;

public interface EventService {

    List<EventDTO> listEvents(int year, int month, UUID userId);
}
