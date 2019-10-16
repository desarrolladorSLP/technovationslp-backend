package org.desarrolladorslp.technovation.controller;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.desarrolladorslp.technovation.dto.BatchDTO;
import org.desarrolladorslp.technovation.dto.RegisterToBatchDTO;
import org.desarrolladorslp.technovation.models.Batch;
import org.desarrolladorslp.technovation.services.BatchService;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
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

    private ModelMapper modelMapper;

    private BatchService batchService;

    @Secured({"ROLE_ADMINISTRATOR"})
    @PostMapping
    public ResponseEntity<BatchDTO> save(@RequestBody BatchDTO batchDTO) {

        Batch batch = convertToEntity(batchDTO);
        batch.setId(null);

        return new ResponseEntity<>(convertToDTO(batchService.save(batch)), HttpStatus.CREATED);
    }

    @Secured({"ROLE_ADMINISTRATOR"})
    @PutMapping
    public ResponseEntity<BatchDTO> update(@RequestBody BatchDTO batchDTO) {

        Batch batch = convertToEntity(batchDTO);
        if (Objects.isNull(batch.getId())) {
            throw new IllegalArgumentException("id must not be null");
        }

        return new ResponseEntity<>(convertToDTO(batchService.save(batch)), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<BatchDTO>> listBatches() {

        List<Batch> batches = batchService.list();

        return new ResponseEntity<>(batches.stream().map(this::convertToDTO).collect(Collectors.toList()), HttpStatus.OK);
    }

    @GetMapping("/{batchId}")
    public ResponseEntity<BatchDTO> getBatch(@PathVariable String batchId) {
        return new ResponseEntity<>(convertToDTO(batchService.findById(UUID.fromString(batchId)).orElseThrow()), HttpStatus.OK);
    }

    @GetMapping("/program/{programId}")
    public ResponseEntity<List<BatchDTO>> getBatchByProgram(@PathVariable String programId) {

        List<Batch> batches = batchService.findByProgram(UUID.fromString(programId));

        return new ResponseEntity<>(batches.stream().map(this::convertToDTO).collect(Collectors.toList()), HttpStatus.OK);
    }

    @Secured({"ROLE_ADMINISTRATOR"})
    @DeleteMapping("/{batchId}")
    public void deleteBatch(@PathVariable UUID batchId) {
        batchService.delete(batchId);
    }

    @Secured({"ROLE_ADMINISTRATOR"})
    @PostMapping("registerUser/{batchId}/{userId}")
    public ResponseEntity registerUserToBatch(@PathVariable String batchId, @PathVariable String userId) {
        batchService.registerUserToBatch(UUID.fromString(batchId), UUID.fromString(userId));
        return new ResponseEntity(HttpStatus.OK);
    }

    @Secured({"ROLE_ADMINISTRATOR"})
    @PostMapping("register")
    public void registerToBatch(@RequestBody RegisterToBatchDTO register) {
        batchService.registerMultipleUsersToBatch(register);
    }

    @Autowired
    public void setBatchService(BatchService batchService) {
        this.batchService = batchService;
    }

    @PostConstruct
    public void prepareMappings() {

        modelMapper.addMappings(new PropertyMap<BatchDTO, Batch>() {
            @Override
            protected void configure() {
                map().getProgram().setId(source.getProgramId());
            }
        });

        modelMapper.addMappings(new PropertyMap<Batch, BatchDTO>() {
            @Override
            protected void configure() {
                map().setProgramId(source.getProgram().getId());
            }
        });
    }

    private Batch convertToEntity(BatchDTO batchDTO) {

        return modelMapper.map(batchDTO, Batch.class);
    }

    private BatchDTO convertToDTO(Batch batch) {

        return modelMapper.map(batch, BatchDTO.class);
    }

    @Autowired
    public void setModelMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }
}
