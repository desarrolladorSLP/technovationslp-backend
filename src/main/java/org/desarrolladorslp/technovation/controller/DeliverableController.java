package org.desarrolladorslp.technovation.controller;

import org.desarrolladorslp.technovation.dto.DeliverableDTO;
import org.desarrolladorslp.technovation.dto.DeliverableToTeckerDTO;
import org.desarrolladorslp.technovation.services.DeliverableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

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

    @Secured({"ROLE_ADMINISTRATOR"})
    @PostMapping("{deliverableId}/teckers")
    public void assignToDeliverable(@PathVariable UUID deliverableId, @RequestBody DeliverableToTeckerDTO teckers) {
        deliverableService.AssignToDeliverable(deliverableId, teckers.getTeckersToAssign());
    }

    @Autowired
    public void setDeliverableService(DeliverableService deliverableService) {
        this.deliverableService = deliverableService;
    }
}
