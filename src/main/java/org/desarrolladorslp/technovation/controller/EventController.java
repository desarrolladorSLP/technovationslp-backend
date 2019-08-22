package org.desarrolladorslp.technovation.controller;

import org.desarrolladorslp.technovation.controller.dto.EventDTO;
import org.desarrolladorslp.technovation.services.EventService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/events/{month}")
public class EventController {

    private EventService eventService;

    @GetMapping
    public ResponseEntity<String> listEvents(@PathVariable String month){

        return new ResponseEntity<>("Hello World!" + month ,HttpStatus.OK);
    }

}
