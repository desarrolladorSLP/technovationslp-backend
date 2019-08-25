package org.desarrolladorslp.technovation.services.impl;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.desarrolladorslp.technovation.models.Batch;
import org.desarrolladorslp.technovation.models.Session;
import org.desarrolladorslp.technovation.repository.SessionRepository;
import org.desarrolladorslp.technovation.services.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public List<Session> list() {
        return sessionRepository.findAll();
    }

    @Override
    @Transactional
    public void confirmAttendance(UUID sessionId, UUID userId){
        sessionRepository.confirmAttendance(sessionId, userId);
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
