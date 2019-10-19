package org.desarrolladorslp.technovation.controller;

import org.desarrolladorslp.technovation.dto.DeliverableDTO;
import org.desarrolladorslp.technovation.services.DeliverableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/deliverable")
public class DeliverableController {

    private DeliverableService deliverableService;

    @Secured({"ROLE_ADMINISTRATOR"})
    @PostMapping
    public ResponseEntity<DeliverableDTO> save(@RequestBody DeliverableDTO deliverableDTO) {
        return new ResponseEntity<>(deliverableService.save(deliverableDTO), HttpStatus.CREATED);
    }

    @GetMapping("/batch/{batchId}")
    public ResponseEntity<List<DeliverableDTO>> getDeliverablesByBatch(@PathVariable UUID batchId) {
        return new ResponseEntity<>(deliverableService.findByBatch(batchId), HttpStatus.OK);
    }

    @Secured({"ROLE_ADMINISTRATOR"})
    @PutMapping("/{deliverableId}")
    public ResponseEntity<DeliverableDTO> update(@RequestBody DeliverableDTO deliverableDTO, @PathVariable UUID deliverableId) {
        return new ResponseEntity<>(deliverableService.update(deliverableDTO, deliverableId), HttpStatus.OK);
    }

    @Autowired
    public void setDeliverableService(DeliverableService deliverableService) {
        this.deliverableService = deliverableService;
    }
}
