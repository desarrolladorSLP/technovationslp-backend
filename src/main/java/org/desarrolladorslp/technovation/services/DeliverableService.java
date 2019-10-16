package org.desarrolladorslp.technovation.services;

import org.desarrolladorslp.technovation.dto.DeliverableDTO;

import java.util.UUID;

public interface DeliverableService {

    DeliverableDTO save(DeliverableDTO deliverableDTO);

    void assignDeliverableToSession(UUID deliverableId, UUID sessionId, String type);
}
