package org.desarrolladorslp.technovation.controller;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.desarrolladorslp.technovation.models.Batch;
import org.desarrolladorslp.technovation.services.BatchService;
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
@RequestMapping("/api/batch")
public class BatchController {

    private BatchService batchService;

    @Secured({"ROLE_ADMINISTRATOR"})
    @PostMapping
    public ResponseEntity<Batch> save(@RequestBody Batch batch) {

        batch.setId(null);

        return new ResponseEntity<>(batchService.save(batch), HttpStatus.CREATED);
    }

    @Secured({"ROLE_ADMINISTRATOR"})
    @PutMapping
    public ResponseEntity<Batch> update(@RequestBody Batch batch) {

        if (Objects.isNull(batch.getId())) {
            throw new IllegalArgumentException("id must not be null");
        }

        return new ResponseEntity<>(batchService.save(batch), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Batch>> listBatches() {

        return new ResponseEntity<>(batchService.list(), HttpStatus.OK);
    }

    @GetMapping
    @RequestMapping("/{batchId}")
    public ResponseEntity<Batch> getBatch(@PathVariable String batchId) {
        return new ResponseEntity<>(batchService.findById(UUID.fromString(batchId)).orElseThrow(), HttpStatus.OK);
    }

    @GetMapping
    @RequestMapping("/program/{programId}")
    public ResponseEntity<List<Batch>> getBatchByProgram(@PathVariable String programId) {

        return new ResponseEntity<>(batchService.findByProgram(UUID.fromString(programId)), HttpStatus.OK);
    }

    @Autowired
    public void setBatchService(BatchService batchService) {
        this.batchService = batchService;
    }
}
