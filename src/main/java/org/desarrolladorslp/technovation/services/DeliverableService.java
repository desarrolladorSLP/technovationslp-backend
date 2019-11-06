package org.desarrolladorslp.technovation.services;

import org.desarrolladorslp.technovation.Enum.RelationType;
import org.desarrolladorslp.technovation.dto.DeliverableDTO;
import org.desarrolladorslp.technovation.dto.ResourceDTO;
import org.desarrolladorslp.technovation.models.Deliverable;
import org.desarrolladorslp.technovation.models.Resource;

import java.util.UUID;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface DeliverableService {

    Optional<Deliverable> findById(UUID id);

    DeliverableDTO save(DeliverableDTO deliverableDTO);

    void removeAssignmentFromThisDeliverableToTecker(UUID deliverableId);

    void assignToDeliverable(UUID deliverableId, UUID tekerId);

    void assignToDeliverable(UUID deliverableId, List<UUID> teckersToAssign);

    void assignTeckerToDeliverable(UUID deliverableId, UUID batchId, List<UUID> teckersToAssign);

    List<DeliverableDTO> findByBatch(UUID batchId);

    DeliverableDTO update(DeliverableDTO deliverableDTO, UUID deliverableId);

    void delete(UUID deliverableId);

    void assignDeliverableToSession(UUID deliverableId, UUID sessionId, RelationType type);

    void addResourcesToDeliverable(UUID deliverableIds, List<Resource> resource);

    List<Resource> getResourcesByDeliverable(UUID deliverableId);

    void deleteResourceFromDeliverable(UUID deliverableId, UUID resourceId);

}
