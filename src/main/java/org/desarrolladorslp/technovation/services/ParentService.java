package org.desarrolladorslp.technovation.services;

import org.desarrolladorslp.technovation.dto.TeckerDTO;

import java.util.List;
import java.util.UUID;

public interface ParentService {

    void assignToParent(UUID parentId, List<UUID> teckers);

    void assignTeckersToParent(UUID parentId, List<UUID> teckers);

    List<TeckerDTO> teckersByParent(UUID parentId);

    List<TeckerDTO> teckerByParentLogged(UUID parentId);
}
