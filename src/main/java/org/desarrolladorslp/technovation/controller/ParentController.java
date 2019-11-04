package org.desarrolladorslp.technovation.controller;

import org.desarrolladorslp.technovation.dto.TeckerDTO;
import org.desarrolladorslp.technovation.dto.TeckersToParentDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/parent")
public class ParentController {

    @PutMapping("/{parentId}")
    public ResponseEntity<HttpStatus> assignTeckersToParent(@PathVariable UUID parentId, @RequestBody TeckersToParentDTO teckers){
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @GetMapping("/{parentId}")
    public ResponseEntity<List<TeckerDTO>> getTeckersByParent(@PathVariable UUID parentId){

        List<TeckerDTO> teckers = new ArrayList<>();

        teckers.add(TeckerDTO.builder()
                .teckerId(UUID.randomUUID())
                .name("Tecker 1")
                .pictureUrl("fake-pictures/teckers1.jpg").build());

        teckers.add(TeckerDTO.builder()
                .teckerId(UUID.randomUUID())
                .name("Tecker 2")
                .pictureUrl("fake-pictures/teckers2.jpg").build());

        return new ResponseEntity<>(teckers,HttpStatus.OK);
    }

    @GetMapping("/teckers")
    public ResponseEntity<List<TeckerDTO>> getTeckers(Principal principal){
        List<TeckerDTO> teckers = new ArrayList<>();

        teckers.add(TeckerDTO.builder()
                .teckerId(UUID.randomUUID())
                .name("Tecker 1")
                .pictureUrl("fake-pictures/teckers1.jpg").build());

        teckers.add(TeckerDTO.builder()
                .teckerId(UUID.randomUUID())
                .name("Tecker 2")
                .pictureUrl("fake-pictures/teckers2.jpg").build());

        teckers.add(TeckerDTO.builder()
                .teckerId(UUID.randomUUID())
                .name("Tecker 3")
                .pictureUrl("fake-pictures/teckers3.jpg").build());

        return new ResponseEntity<>(teckers,HttpStatus.OK);
    }
}
