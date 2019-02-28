package org.desarrolladorslp.technovation.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.desarrolladorslp.technovation.models.Program;

public interface ProgramService {

    Program save(Program program);

    List<Program> list();

    Optional<Program> findById(UUID id);
}
