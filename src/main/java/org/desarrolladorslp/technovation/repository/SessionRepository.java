package org.desarrolladorslp.technovation.repository;

import java.util.List;
import java.util.UUID;

import org.desarrolladorslp.technovation.models.Batch;
import org.desarrolladorslp.technovation.models.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionRepository extends JpaRepository<Session, UUID> {

    List<Session> findByBatch(Batch batch);
}
