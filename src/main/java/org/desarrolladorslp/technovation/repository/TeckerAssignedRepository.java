package org.desarrolladorslp.technovation.repository;

import org.desarrolladorslp.technovation.models.TeckerAssigned;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface TeckerAssignedRepository extends JpaRepository<TeckerAssigned, UUID> {

    @Modifying
    @Query(value = "DELETE FROM tecker_by_deliverable WHERE deliverable_id = :deliverableId", nativeQuery = true)
    void removeAssignment(UUID deliverableId);
}
