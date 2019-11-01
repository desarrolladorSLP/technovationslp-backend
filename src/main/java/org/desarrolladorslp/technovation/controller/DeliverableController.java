package org.desarrolladorslp.technovation.controller;

import org.desarrolladorslp.technovation.dto.DeliverableDTO;
import org.desarrolladorslp.technovation.dto.DeliverableResourcesDTO;
import org.desarrolladorslp.technovation.dto.DeliverableToTeckerDTO;
import org.desarrolladorslp.technovation.dto.ResourceDTO;
import org.desarrolladorslp.technovation.enumerable.StatusType;
import org.desarrolladorslp.technovation.models.Resource;
import org.desarrolladorslp.technovation.services.DeliverableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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

    @Secured({"ROLE_ADMINISTRATOR"})
    @PostMapping("{deliverableId}/teckers")
    public void assignToDeliverable(@PathVariable UUID deliverableId, @RequestBody DeliverableToTeckerDTO teckers) {
        deliverableService.assignToDeliverable(deliverableId, teckers.getTeckersToAssign());
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

    @Secured({"ROLE_ADMINISTRATOR"})
    @DeleteMapping("/{deliverableId}")
    public void deleteDeliverable(@PathVariable UUID deliverableId) {
        deliverableService.delete(deliverableId);
    }

    @GetMapping("/{deliverableId}")
    public ResponseEntity<DeliverableResourcesDTO> getDeliverable(@PathVariable UUID deliverableId) {
        DeliverableResourcesDTO deliverable = new DeliverableResourcesDTO();

        deliverable.setId(UUID.randomUUID());
        deliverable.setTitle("Title 1");
        deliverable.setDueDate("09-10-19");
        deliverable.setDescription("Description");
        deliverable.setBatchId(UUID.randomUUID());

        List<ResourceDTO> resources = new ArrayList<>();

        resources.add(ResourceDTO.builder()
                .url("/fake-resources/img1.jpg")
                .mimeType("image/jpg")
                .build());

        resources.add(ResourceDTO.builder()
                .url("/fake-resources/img2.jpg")
                .mimeType("image/jpg")
                .build());

        deliverable.setResources(resources);
        deliverable.setStatus(StatusType.IN_PROGRESS);

        return new ResponseEntity<>(deliverable, HttpStatus.OK);

    }


    @PutMapping("/{deliverableId}/resources")
    public ResponseEntity<List<Resource>> addResourcesToDeliverable(@PathVariable UUID deliverableId, @RequestBody List<ResourceDTO> resources) {
        List<Resource> newResources = new ArrayList<>();

        newResources.add(Resource.builder().id(UUID.randomUUID())
                .url(resources.get(0).getUrl())
                .mimeType(resources.get(0).getMimeType())
                .build());

        newResources.add(Resource.builder().id(UUID.randomUUID())
                .url(resources.get(1).getUrl())
                .mimeType(resources.get(1).getMimeType())
                .build());

        return new ResponseEntity<>(newResources, HttpStatus.OK);
    }

    @GetMapping("/{deliverableId}/resources")
    public ResponseEntity<List<Resource>> getResourcesByDeliverable(@PathVariable UUID deliverableId) {
        List<Resource> resources = new ArrayList<>();

        resources.add(Resource.builder()
                .id(UUID.randomUUID())
                .url("/fake-resources/img1.jpg")
                .mimeType("image/jpg").build());

        resources.add(Resource.builder()
                .id(UUID.randomUUID())
                .url("/fake-resources/img2.jpg")
                .mimeType("image/jpg").build());

        return new ResponseEntity(resources, HttpStatus.OK);
    }

    @DeleteMapping("/{deliverableId}/resources/{resourceId}")
    public void deleteResourceFromDeliverable(@PathVariable UUID deliverableId, @PathVariable UUID resourceId) {

    }

    @Autowired
    public void setDeliverableService(DeliverableService deliverableService) {
        this.deliverableService = deliverableService;
    }
}
