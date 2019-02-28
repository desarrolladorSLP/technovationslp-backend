package org.desarrolladorslp.technovation.repository;

import java.util.UUID;

import org.desarrolladorslp.technovation.models.Program;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProgramRepository extends JpaRepository<Program, UUID> {
}
