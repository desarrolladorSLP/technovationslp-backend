package org.desarrolladorslp.technovation.services.impl;

import org.desarrolladorslp.technovation.exception.UserAlreadyConfirmedException;
import org.desarrolladorslp.technovation.models.Batch;
import org.desarrolladorslp.technovation.models.Session;
import org.desarrolladorslp.technovation.models.User;
import org.desarrolladorslp.technovation.repository.SessionRepository;
import org.desarrolladorslp.technovation.services.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class SessionServiceImpl implements SessionService {
    private SessionRepository sessionRepository;

    @Override
    @Transactional
    public Session save(Session session) {
        if (Objects.isNull(session.getId())) {
            session.setId(UUID.randomUUID());
        }
        return sessionRepository.save(session);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> allPeople(UUID sessionId) {
        return sessionRepository.allPeople(sessionId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> staff(UUID sessionId) {
        return sessionRepository.staff(sessionId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Session> list() {
        return sessionRepository.findAll();
    }

    @Override
    @Transactional
    public void confirmAttendance(UUID sessionId, UUID userId) {
        sessionRepository.getUserBySession(sessionId, userId).ifPresentOrElse(
                user -> {
                    throw new UserAlreadyConfirmedException(user.getId() + " has been confirmed already");
                },
                () -> sessionRepository.confirmAttendance(sessionId, userId));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Session> findById(UUID id) {
        return sessionRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Session> findByBatch(UUID batchId) {
        Batch batch = new Batch();

        batch.setId(batchId);

        return sessionRepository.findByBatch(batch);
    }

    @Autowired
    public void setSessionRepository(SessionRepository batchRepository) {
        this.sessionRepository = batchRepository;
    }
}
