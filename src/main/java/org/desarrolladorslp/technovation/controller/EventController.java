package org.desarrolladorslp.technovation.controller;

import org.desarrolladorslp.technovation.controller.dto.EventDTO;
import org.desarrolladorslp.technovation.services.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/events/{year}/{month}")
public class EventController {

    private EventService eventService;

    @GetMapping
    public ResponseEntity<List<EventDTO>> listEvents(@PathVariable String month, @PathVariable String year){

        return new ResponseEntity<>(eventService.list(Integer.parseInt(year),Integer.parseInt(month)),HttpStatus.OK);
    }

    @Autowired
    public void setEventService(EventService eventService){ this.eventService = eventService;}

}
