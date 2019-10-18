package org.desarrolladorslp.technovation.services;

import org.desarrolladorslp.technovation.dto.DeliverableDTO;

import java.util.List;
import java.util.UUID;

public interface DeliverableService {

    DeliverableDTO save(DeliverableDTO deliverableDTO);

    List<DeliverableDTO> findByBatch(UUID batchId);
}
