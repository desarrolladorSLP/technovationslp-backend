package org.desarrolladorslp.technovation.repository;

import org.desarrolladorslp.technovation.Enum.RelationType;
import org.desarrolladorslp.technovation.models.Deliverable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DeliverableRepository extends JpaRepository<Deliverable, UUID> {

    @Modifying
    @Query(value = "INSERT INTO deliverables_by_session (deliverable_id, session_id, type) VALUES (:deliverableId, :sessionId, :type)", nativeQuery = true)
    void assignDeliverableToSession(UUID deliverableId, UUID sessionId, String type);
}
