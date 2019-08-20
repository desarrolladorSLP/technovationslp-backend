package org.desarrolladorslp.technovation.controller;

import java.util.*;
import java.util.stream.Collectors;

import org.desarrolladorslp.technovation.controller.dto.SessionDTO;
import org.desarrolladorslp.technovation.models.Session;
import org.desarrolladorslp.technovation.services.SessionService;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

@RestController
@RequestMapping("/api/session")
public class SessionController {

    private ModelMapper modelMapper;

    private SessionService sessionService;

    @Secured({"ROLE_ADMINISTRATOR"})
    @PostMapping
    public ResponseEntity<SessionDTO> save(@RequestBody SessionDTO sessionDTO) {

        Session session = convertToEntity(sessionDTO);
        session.setId(null);

        return new ResponseEntity<>(convertToDTO(sessionService.save(session)), HttpStatus.CREATED);
    }

    @Secured({"ROLE_ADMINISTRATOR"})
    @PutMapping
    public ResponseEntity<SessionDTO> update(@RequestBody SessionDTO sessionDTO) {

        Session session = convertToEntity(sessionDTO);
        if (Objects.isNull(session.getId())) {
            throw new IllegalArgumentException("id must not be null");
        }

        return new ResponseEntity<>(convertToDTO(sessionService.save(session)), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<SessionDTO>> listSessions() {

        List<Session> sessions = sessionService.list();

        return new ResponseEntity<>(sessions.stream().map(this::convertToDTO).collect(Collectors.toList()), HttpStatus.OK);
    }

    @GetMapping
    @RequestMapping("/{sessionId}")
    public ResponseEntity<SessionDTO> getSession(@PathVariable String sessionId) {

        return new ResponseEntity<>(convertToDTO(sessionService.findById(UUID.fromString(sessionId)).orElseThrow()), HttpStatus.OK);
    }

    @Autowired
    public void setSessionService(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @GetMapping
    @RequestMapping("batch/{batchId}")
    public ResponseEntity<List<SessionDTO>> getSessionsByBatch(@PathVariable String batchId) {

        List<Session> sessions = sessionService.findByBatch(UUID.fromString(batchId));

        return new ResponseEntity<>(sessions.stream().map(this::convertToDTO).collect(Collectors.toList()), HttpStatus.OK);
    }

    public Session convertToEntity(SessionDTO sessionDTO) {

        return modelMapper.map(sessionDTO, Session.class);
    }

    public SessionDTO convertToDTO(Session session) {

        return modelMapper.map(session, SessionDTO.class);
    }

    @Autowired
    public void setModelMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @PostConstruct
    public void prepareMappings(){

        modelMapper.addMappings(new PropertyMap<SessionDTO, Session>() {
            @Override
            protected void configure() {
                map().getBatch().setId(source.getBatchId());
            }
        });

        modelMapper.addMappings(new PropertyMap<Session, SessionDTO>() {
            @Override
            protected void configure() {
                map().setBatchId(source.getBatch().getId());
            }
        });
    }
}
