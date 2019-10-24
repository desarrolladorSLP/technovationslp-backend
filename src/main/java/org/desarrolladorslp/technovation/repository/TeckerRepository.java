package org.desarrolladorslp.technovation.repository;


import org.desarrolladorslp.technovation.models.DeliverableByTecker;
import org.desarrolladorslp.technovation.models.TeckerAssigned;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TeckerRepository extends JpaRepository<TeckerAssigned, UUID> {

    @Query("SELECT new org.desarrolladorslp.technovation.models.DeliverableByTecker(ta.id, d.title, d.dueDate, ta.status) FROM TeckerAssigned ta INNER JOIN Deliverable d ON ta.deliverableId = d.id WHERE ta.teckerId = :teckerId")
    List<DeliverableByTecker> deliverableByTecker(UUID teckerId);

    @Query(value = "SELECT DISTINCT true FROM teckers_by_parents WHERE tecker_id = :teckerId AND parent_id = :parentId", nativeQuery = true)
    Optional<Boolean> areFamily(UUID teckerId, UUID parentId);

}
