package org.desarrolladorslp.technovation.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.desarrolladorslp.technovation.models.Program;
import org.desarrolladorslp.technovation.repository.ProgramRepository;
import org.desarrolladorslp.technovation.services.ProgramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProgramServiceImpl implements ProgramService {

    private ProgramRepository programRepository;

    @Override
    public Program save(Program program) {
        return programRepository.save(program);
    }

    @Override
    public List<Program> list() {
        return programRepository.findAll();
    }

    public Optional<Program> findById(UUID id) {
        return programRepository.findById(id);
    }

    @Autowired
    public void setProgramRepository(ProgramRepository programRepository) {
        this.programRepository = programRepository;
    }
}
