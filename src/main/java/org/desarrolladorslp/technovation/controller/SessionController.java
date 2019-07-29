package org.desarrolladorslp.technovation.controller;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.desarrolladorslp.technovation.models.Session;
import org.desarrolladorslp.technovation.services.SessionService;
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

@RestController
@RequestMapping("/api/session")
public class SessionController {

    private SessionService sessionService;

    @Secured({"ROLE_ADMINISTRATOR"})
    @PostMapping
    public ResponseEntity<Session> save(@RequestBody Session session) {

        session.setId(null);

        return new ResponseEntity<>(sessionService.save(session), HttpStatus.CREATED);
    }

    @Secured({"ROLE_ADMINISTRATOR"})
    @PutMapping
    public ResponseEntity<Session> update(@RequestBody Session session) {

        if (Objects.isNull(session.getId())) {
            throw new IllegalArgumentException("id must not be null");
        }

        return new ResponseEntity<>(sessionService.save(session), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Session>> listSessions() {

        return new ResponseEntity<>(sessionService.list(), HttpStatus.OK);
    }

    @GetMapping
    @RequestMapping("/{sessionId}")
    public ResponseEntity<Session> getSession(@PathVariable String sessionId) {

        return new ResponseEntity<>(sessionService.findById(UUID.fromString(sessionId)).orElseThrow(), HttpStatus.OK);
    }

    @Autowired
    public void setSessionService(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @GetMapping
    @RequestMapping("batch/{batchId}")
    public ResponseEntity<List<Session>> getSessionsByBatch(@PathVariable String batchId) {

        return new ResponseEntity<>(sessionService.findByBatch(UUID.fromString(batchId)), HttpStatus.OK);
    }
}
