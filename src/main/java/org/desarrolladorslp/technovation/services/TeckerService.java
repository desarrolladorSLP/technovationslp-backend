package org.desarrolladorslp.technovation.services;

import org.desarrolladorslp.technovation.dto.DeliverableByTeckerDTO;

import java.util.List;
import java.util.UUID;

public interface TeckerService {

    List<DeliverableByTeckerDTO> getDeliverableByTecker(UUID teckerId);

    List<DeliverableByTeckerDTO> getDeliverableByKid(UUID teckerId, UUID parentId);


}
