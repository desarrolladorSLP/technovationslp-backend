package org.desarrolladorslp.technovation.services.impl;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.desarrolladorslp.technovation.models.Program;
import org.desarrolladorslp.technovation.repository.ProgramRepository;
import org.desarrolladorslp.technovation.services.ProgramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProgramServiceImpl implements ProgramService {

    private ProgramRepository programRepository;

    @Override
    @Transactional
    public Program save(Program program) {
        if (Objects.isNull(program.getId())) {
            program.setId(UUID.randomUUID());
        }
        return programRepository.save(program);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Program> list() {
        return programRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Program> findById(UUID id) {
        return programRepository.findById(id);
    }

    @Override
    @Transactional
    public Optional<Program> delete(UUID id) {
        Optional<Program> optionalProgram = programRepository.findById(id);

        optionalProgram.ifPresent(p -> programRepository.delete(p));

        return optionalProgram;
    }

    @Autowired
    public void setProgramRepository(ProgramRepository programRepository) {
        this.programRepository = programRepository;
    }
}
