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

@Service
public class EventServiceImpl implements EventService {

    private SessionService sessionService;

    private ModelMapper modelMapper;

    @Override
    public List<EventDTO> list(int month) {

        if(month > 0 && month <13){
            //SessionService
        }
        return null;
    }

    @PostConstruct
    public void prepareMappings(){

        modelMapper.addMappings(new PropertyMap<Session, EventDTO>() {
            @Override
            protected void configure(){
                map().setMonth(source.getDate().getMonthValue());
                map().setDay(source.getDate().getDayOfMonth());
                map().setSubject(source.getTitle());
                map().setDirections(source.getNotes());
            }
        });
    }

    public EventDTO convertToDTO(Session session){

        return modelMapper.map(session, EventDTO.class);
    }

    @Autowired
    public void setModelMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }
}
