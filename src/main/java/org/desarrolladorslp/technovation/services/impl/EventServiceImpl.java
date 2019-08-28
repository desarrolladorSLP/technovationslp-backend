package org.desarrolladorslp.technovation.services.impl;

import org.desarrolladorslp.technovation.controller.dto.EventDTO;
import org.desarrolladorslp.technovation.models.Session;
import org.desarrolladorslp.technovation.services.EventService;
import org.desarrolladorslp.technovation.services.SessionService;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventServiceImpl implements EventService {

    private SessionService sessionService;

    private ModelMapper modelMapper;

    @Override
    public List<EventDTO> list(int year, int month) {

        if (month < 1 || month > 12) {
            throw new IllegalArgumentException("Invalid Month");
        }

        List<Session> sessions = sessionService.list();
        return sessions.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @PostConstruct
    public void prepareMappings() {

        modelMapper.addMappings(new PropertyMap<Session, EventDTO>() {
            @Override
            protected void configure() {
                map().setType("SESSION");
                map().setSubject(source.getTitle());
                map().setDirections(source.getNotes());
                map().setDate(source.getDate());
            }
        });
    }

    public EventDTO convertToDTO(Session session) {

        return modelMapper.map(session, EventDTO.class);
    }

    @Autowired
    public void setSessionService(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Autowired
    public void setModelMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }
}
