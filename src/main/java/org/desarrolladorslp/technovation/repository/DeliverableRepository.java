package org.desarrolladorslp.technovation.repository;

import org.desarrolladorslp.technovation.dto.DeliverableResourcesDTO;
import org.desarrolladorslp.technovation.dto.ResourceDTO;
import org.desarrolladorslp.technovation.models.Batch;
import org.desarrolladorslp.technovation.models.Deliverable;
import org.desarrolladorslp.technovation.models.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DeliverableRepository extends JpaRepository<Deliverable, UUID> {


    List<Deliverable> findByBatch(Batch batch);

    @Modifying
    @Query(value = "INSERT INTO deliverables_by_session (deliverable_id, session_id, type) " +
            "SELECT :deliverableId, s.id, :type FROM sessions s WHERE s.id = :sessionId AND batch_id = (" +
            "SELECT batch_id FROM deliverables WHERE id = :deliverableId)"
            , nativeQuery = true)
    int assignDeliverableToSession(UUID deliverableId, UUID sessionId, String type);

    @Modifying
    @Query(value = "INSERT INTO deliverables_resources (deliverable_id, resource_id) VALUES (:deliverableId, :resourceId)", nativeQuery = true)
    void addResourceToDeliverable(UUID deliverableId, UUID resourceId);

    @Query("SELECT r FROM Resource r JOIN DeliverableResources dr ON dr.deliverableId = :deliverableId WHERE r.id = dr.resourceId")
    List<Resource> getResourcesByDeliverable(UUID deliverableId);

    @Modifying
    @Query(value = "DELETE FROM deliverables_resources WHERE resource_id = :resourceId AND deliverable_id = :deliverableId", nativeQuery = true)
    void deleteResourceFromDeliverable(UUID deliverableId, UUID resourceId);

    @Query("SELECT new org.desarrolladorslp.technovation.dto.DeliverableResourcesDTO(d.id, d.dueDate, d.title, d.description,ta.status ) FROM Deliverable d JOIN TeckerAssignment ta ON d.id = ta.deliverableId WHERE d.id = :deliverableId")
    DeliverableResourcesDTO getDeliverable(UUID deliverableId);
}
