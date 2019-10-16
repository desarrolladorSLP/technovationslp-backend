package org.desarrolladorslp.technovation.repository;

import org.desarrolladorslp.technovation.models.Deliverable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DeliverableRepository extends JpaRepository<Deliverable, UUID> {

}
