package org.desarrolladorslp.technovation.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.desarrolladorslp.technovation.models.Session;
import org.desarrolladorslp.technovation.models.User;

public interface SessionService {
    Session save(Session session);

    List<Session> list();

    Optional<Session> findById(UUID id);

    List<Session> findByBatch(UUID batchId);

    void confirmAttendance(UUID sessionId, UUID userId);

    List<User> allPeople(UUID sessionId);

    List<User> staff(UUID sessionId);


}
