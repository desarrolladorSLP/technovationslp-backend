package org.desarrolladorslp.technovation.services.impl;

import org.desarrolladorslp.technovation.controller.dto.EventDTO;
import org.desarrolladorslp.technovation.models.Session;
import org.desarrolladorslp.technovation.services.EventService;
import org.desarrolladorslp.technovation.services.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventServiceImpl implements EventService {

    private SessionService sessionService;

    @Override
    public List<EventDTO> list(int year, int month) {

        if(month < 1 || month > 12){
            throw new IllegalArgumentException("Invalid Month");
        }

        List<Session> sessions = sessionService.list();
        return sessions.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public EventDTO convertToDTO(Session session){

        EventDTO eventDTO = new EventDTO();
        eventDTO.setType("SESSION");
        eventDTO.setDay(session.getDate().getDayOfMonth());
        eventDTO.setMonth(session.getDate().getMonthValue());
        eventDTO.setSubject(session.getTitle());
        eventDTO.setLocation(session.getLocation());
        eventDTO.setStartTime(session.getStartTime());
        eventDTO.setEndTime(session.getEndTime());
        eventDTO.setDirections(session.getNotes());
        return  eventDTO;
    }

    @Autowired
    public void setSessionService(SessionService sessionService){
        this.sessionService = sessionService;
    }
}
