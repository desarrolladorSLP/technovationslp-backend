package org.desarrolladorslp.technovation.controller;

import org.desarrolladorslp.technovation.config.auth.TokenInfoService;
import org.desarrolladorslp.technovation.controller.dto.EventDTO;
import org.desarrolladorslp.technovation.services.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("api/events")
public class EventController {

    private EventService eventService;

    private TokenInfoService tokenInfoService;

    @GetMapping
    @RequestMapping("/{year}/{month}")
    public ResponseEntity<List<EventDTO>> listEvents(@PathVariable int month, @PathVariable int year){

        return new ResponseEntity<>(eventService.list(year,month),HttpStatus.OK);
    }

    @Autowired
    public void setEventService(EventService eventService){ this.eventService = eventService;}

    @Autowired
    public void setTokenInfoService(TokenInfoService tokenInfoService){
        this.tokenInfoService = tokenInfoService;
    }

}
