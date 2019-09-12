package org.desarrolladorslp.technovation.controller;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.desarrolladorslp.technovation.models.Program;
import org.desarrolladorslp.technovation.services.ProgramService;
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
@RequestMapping("/api/program")
public class ProgramController {

    private ProgramService programService;

    @Secured({"ROLE_ADMINISTRATOR"})
    @PostMapping
    public ResponseEntity<Program> save(@RequestBody Program program) {

        program.setId(null);

        return new ResponseEntity<>(programService.save(program), HttpStatus.CREATED);
    }

    @Secured({"ROLE_ADMINISTRATOR"})
    @PutMapping
    public ResponseEntity<Program> update(@RequestBody Program program) {

        if (Objects.isNull(program.getId())) {
            throw new IllegalArgumentException("id must not be null");
        }

        return new ResponseEntity<>(programService.save(program), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Program>> listPrograms() {

        return new ResponseEntity<>(programService.list(), HttpStatus.OK);
    }

    @GetMapping("/{programId}")
    public ResponseEntity<Program> getProgram(@PathVariable String programId) {

        return new ResponseEntity<>(programService.findById(UUID.fromString(programId)).orElseThrow(), HttpStatus.OK);
    }

    @DeleteMapping("/{programId}")
    public ResponseEntity<Program> deleteProgram(@PathVariable String programId) {

        return new ResponseEntity<>(programService.delete(UUID.fromString(programId)).orElseThrow(), HttpStatus.OK);
    }

    @Autowired
    public void setProgramService(ProgramService programService) {
        this.programService = programService;
    }
}
