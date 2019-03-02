package org.desarrolladorslp.technovation.repository;

import java.util.List;
import java.util.UUID;

import org.desarrolladorslp.technovation.models.Batch;
import org.desarrolladorslp.technovation.models.Program;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BatchRepository extends JpaRepository<Batch, UUID> {

    List<Batch> findByProgram(Program program);
}
