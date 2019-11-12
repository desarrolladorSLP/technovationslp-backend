package org.desarrolladorslp.technovation.controller;

import org.desarrolladorslp.technovation.dto.AssignTeckersDTO;
import org.desarrolladorslp.technovation.dto.TeckerDTO;
import org.desarrolladorslp.technovation.services.MentorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/mentor")
public class MentorController {

    private MentorService mentorService;

    @PutMapping("/{mentorId}")
    public void assignTeckersToMentor(@PathVariable UUID mentorId, @RequestBody AssignTeckersDTO teckers) {
        mentorService.assignTeckersToMentor(mentorId, teckers);
    }

    @Secured("ROLE_ADMINISTRATOR")
    @GetMapping("/{mentorId}/teckers")
    public ResponseEntity<List<TeckerDTO>> getTeckersByMentor(@PathVariable UUID mentorId) {

        List<TeckerDTO> teckers = new ArrayList<>();

        teckers.add(TeckerDTO.builder()
                .teckerId(UUID.randomUUID())
                .name("Tecker 1")
                .pictureUrl("/fake-pictures/tecker1.jpg").build());

        teckers.add(TeckerDTO.builder()
                .teckerId(UUID.randomUUID())
                .name("Tecker 2")
                .pictureUrl("/fake-pictures/tecker2.jpg").build());

        return new ResponseEntity<>(mentorService.getTeckersByMentor(mentorId), HttpStatus.OK);
    }

    @GetMapping("/teckers")
    public ResponseEntity<List<TeckerDTO>> getTeckers(Principal principal) {
        List<TeckerDTO> teckers = new ArrayList<>();

        teckers.add(TeckerDTO.builder()
                .teckerId(UUID.randomUUID())
                .name("Tecker 1")
                .pictureUrl("/fake-pictures/tecker1.jpg").build());

        teckers.add(TeckerDTO.builder()
                .teckerId(UUID.randomUUID())
                .name("Tecker 2")
                .pictureUrl("/fake-pictures/tecker2.jpg").build());

        return new ResponseEntity<>(teckers, HttpStatus.OK);
    }

    @Autowired
    private void setMentorService(MentorService mentorService) {
        this.mentorService = mentorService;
    }

}
