package org.desarrolladorslp.technovation.services;

import org.desarrolladorslp.technovation.Enum.RelationType;
import org.desarrolladorslp.technovation.dto.DeliverableDTO;

import java.util.UUID;

import java.util.List;
import java.util.UUID;

public interface DeliverableService {

    DeliverableDTO save(DeliverableDTO deliverableDTO);

    List<DeliverableDTO> findByBatch(UUID batchId);

    DeliverableDTO update(DeliverableDTO deliverableDTO, UUID deliverableId);

    void delete(UUID deliverableId);

    void assignDeliverableToSession(UUID deliverableId, UUID sessionId, RelationType type);

}
