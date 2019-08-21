package org.desarrolladorslp.technovation.controller;

import java.security.Principal;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.desarrolladorslp.technovation.config.auth.TokenInfo;
import org.desarrolladorslp.technovation.models.Session;
import org.desarrolladorslp.technovation.models.User;
import org.desarrolladorslp.technovation.services.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
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

    @Secured({"ROLE_TECKER", "ROLE_STAFF", "ROLE_MENTOR"})
    @PostMapping
    @RequestMapping("/confirm/{sessionId}")
    public ResponseEntity confirmAttendance(@PathVariable String sessionId, Principal principal) {
        OAuth2Authentication auth = (OAuth2Authentication) principal;
        OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) auth.getDetails();
        TokenInfo tokenInfo = (TokenInfo) details.getDecodedDetails();
        String id = tokenInfo.getUserId();
        sessionService.confirmAttendance(UUID.fromString(sessionId), UUID.fromString(id));
        return new ResponseEntity(HttpStatus.OK);
    }

    @Secured({"ROLE_PARENT"})
    @PostMapping
    @RequestMapping("/confirmParent/{sessionId}")
    public ResponseEntity confirmParentAttendanceByTecker(@PathVariable String sessionId, @RequestBody User userTecker) {
        sessionService.confirmAttendance(UUID.fromString(sessionId), userTecker.getId());
        return new ResponseEntity(HttpStatus.OK);
    }

    @Secured({"ROLE_ADMINISTRATOR", "ROLE_STAFF", "ROLE_MENTOR"})
    @GetMapping
    @RequestMapping("/people/{sessionId}")
    public ResponseEntity<List<User>> listAllBySession(@PathVariable String sessionId){
        return new ResponseEntity<>(sessionService.allPeople(UUID.fromString(sessionId)),HttpStatus.OK);
    }

    @Secured({"ROLE_PARENT", "ROLE_TECKER"})
    @GetMapping
    @RequestMapping("/staff/{sessionId}")
    public ResponseEntity<List<User>> listStaffBySession(@PathVariable String sessionId){
        return new ResponseEntity<>(sessionService.staff(UUID.fromString(sessionId)), HttpStatus.OK);
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
    @RequestMapping("/program/{programId}")
    public ResponseEntity<List<Session>> getBatchByProgram(@PathVariable String batchId) {

        return new ResponseEntity<>(sessionService.findByBatch(UUID.fromString(batchId)), HttpStatus.OK);
    }
}
