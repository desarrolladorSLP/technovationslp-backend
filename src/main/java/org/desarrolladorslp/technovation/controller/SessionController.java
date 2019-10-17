package org.desarrolladorslp.technovation.controller;

import java.security.Principal;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.desarrolladorslp.technovation.config.auth.TokenInfoService;
import org.desarrolladorslp.technovation.dto.SessionDTO;
import org.desarrolladorslp.technovation.models.Session;
import org.desarrolladorslp.technovation.models.User;
import org.desarrolladorslp.technovation.services.SessionService;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/session")
public class SessionController {

    private ModelMapper modelMapper;

    private SessionService sessionService;

    private TokenInfoService tokenInfoService;

    @Secured({"ROLE_ADMINISTRATOR"})
    @PostMapping
    public ResponseEntity<SessionDTO> save(@RequestBody SessionDTO sessionDTO) {

        Session session = convertToEntity(sessionDTO);
        session.setId(null);

        return new ResponseEntity<>(convertToDTO(sessionService.save(session)), HttpStatus.CREATED);
    }

    @Secured({"ROLE_TECKER", "ROLE_STAFF", "ROLE_MENTOR"})
    @PostMapping("/confirm/{sessionId}")
    public ResponseEntity confirmAttendance(@PathVariable String sessionId, Principal principal) {
        sessionService.confirmAttendance(UUID.fromString(sessionId), tokenInfoService.getIdFromPrincipal(principal));

        return new ResponseEntity(HttpStatus.OK);
    }

    @Secured({"ROLE_PARENT"})
    @PostMapping("/confirmParent/{sessionId}")
    public ResponseEntity confirmParentAttendanceByTecker(@PathVariable String sessionId, @RequestBody User userTecker) {
        sessionService.confirmAttendance(UUID.fromString(sessionId), userTecker.getId());
        return new ResponseEntity(HttpStatus.OK);
    }

    @Secured({"ROLE_ADMINISTRATOR", "ROLE_STAFF", "ROLE_MENTOR"})
    @GetMapping("/people/{sessionId}")
    public ResponseEntity<List<User>> listAllBySession(@PathVariable String sessionId) {
        return new ResponseEntity<>(sessionService.allPeople(UUID.fromString(sessionId)), HttpStatus.OK);
    }

    @Secured({"ROLE_PARENT", "ROLE_TECKER"})
    @GetMapping("/staff/{sessionId}")
    public ResponseEntity<List<User>> listStaffBySession(@PathVariable String sessionId) {
        return new ResponseEntity<>(sessionService.staff(UUID.fromString(sessionId)), HttpStatus.OK);
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

    @Secured({"ROLE_ADMINISTRATOR"})
    @DeleteMapping("/{sessionId}")
    public void deleteSession(@PathVariable UUID sessionId){
        sessionService.delete(sessionId);
    }


    @GetMapping
    public ResponseEntity<List<SessionDTO>> listSessions() {

        List<Session> sessions = sessionService.list();

        return new ResponseEntity<>(sessions.stream().map(this::convertToDTO).collect(Collectors.toList()), HttpStatus.OK);
    }

    @GetMapping("/{sessionId}")
    public ResponseEntity<SessionDTO> getSession(@PathVariable String sessionId) {

        return new ResponseEntity<>(convertToDTO(sessionService.findById(UUID.fromString(sessionId)).orElseThrow()), HttpStatus.OK);
    }

    @GetMapping("batch/{batchId}")
    public ResponseEntity<List<SessionDTO>> getSessionsByBatch(@PathVariable String batchId) {

        List<Session> sessions = sessionService.findByBatch(UUID.fromString(batchId));

        return new ResponseEntity<>(sessions.stream().map(this::convertToDTO).collect(Collectors.toList()), HttpStatus.OK);
    }

    private Session convertToEntity(SessionDTO sessionDTO) {

        return modelMapper.map(sessionDTO, Session.class);
    }

    private SessionDTO convertToDTO(Session session) {

        return modelMapper.map(session, SessionDTO.class);
    }

    @Autowired
    public void setModelMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @PostConstruct
    public void prepareMappings() {

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

    @Autowired
    public void setSessionService(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Autowired
    public void setTokenInfoService(TokenInfoService tokenInfoService) {
        this.tokenInfoService = tokenInfoService;
    }
}
