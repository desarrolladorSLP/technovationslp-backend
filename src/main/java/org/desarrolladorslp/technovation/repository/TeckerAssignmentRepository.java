package org.desarrolladorslp.technovation.repository;

import org.desarrolladorslp.technovation.models.TeckerAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface TeckerAssignmentRepository extends JpaRepository<TeckerAssignment, UUID> {

    @Modifying
    @Query(value = "DELETE FROM tecker_by_deliverable WHERE deliverable_id = :deliverableId", nativeQuery = true)
    void removeAssignment(UUID deliverableId);

    @Query("SELECT ta FROM TeckerAssigment ta WHERE ta.teckerId = :teckerId ta.deliverableId = :deliverableId")
    Optional<TeckerAssignment> getTeckerAssigmentByTecker(UUID teckerId, UUID deliverableId);
}
