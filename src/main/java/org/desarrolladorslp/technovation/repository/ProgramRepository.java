package org.desarrolladorslp.technovation.repository;

import java.util.UUID;

import org.desarrolladorslp.technovation.models.Program;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProgramRepository extends CrudRepository<Program, UUID> {
}
