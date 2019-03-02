package org.desarrolladorslp.technovation.repository;

import java.util.UUID;

import org.desarrolladorslp.technovation.models.Batch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BatchRepository extends JpaRepository<Batch, UUID> {
}
