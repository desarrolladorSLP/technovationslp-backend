package org.desarrolladorslp.technovation.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.desarrolladorslp.technovation.models.Session;

public interface SessionService {
    Session save(Session session);

    List<Session> list();

    Optional<Session> findById(UUID id);

    List<Session> findByBatch(UUID batchId);
}
