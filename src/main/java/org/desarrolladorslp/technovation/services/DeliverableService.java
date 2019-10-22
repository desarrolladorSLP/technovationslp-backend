package org.desarrolladorslp.technovation.services;

import org.desarrolladorslp.technovation.dto.DeliverableDTO;
import org.desarrolladorslp.technovation.models.Deliverable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DeliverableService {

    Optional<Deliverable> findById(UUID id);

    DeliverableDTO save(DeliverableDTO deliverableDTO);

    void removeAssignmentFromThisDeliverableToTecker(UUID deliverableId);

    void AssignToDeliverable(UUID deliverableId, UUID tekerId);

    void AssignToDeliverable(UUID deliverableId, List<UUID> teckersToAssign);

    void assignTeckerToDeliverable(UUID deliverableId, UUID batchId, List<UUID> teckersToAssign);
}
