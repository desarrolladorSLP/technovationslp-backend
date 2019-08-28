package org.desarrolladorslp.technovation.services;

import org.desarrolladorslp.technovation.controller.dto.EventDTO;

import java.util.List;

public interface EventService {

    List<EventDTO> list(int year, int month);
}
