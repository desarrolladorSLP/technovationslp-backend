package org.desarrolladorslp.technovation.services.impl;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.desarrolladorslp.technovation.exception.UserAlreadyRegisteredInBatch;
import org.desarrolladorslp.technovation.models.Batch;
import org.desarrolladorslp.technovation.models.Program;
import org.desarrolladorslp.technovation.repository.BatchRepository;
import org.desarrolladorslp.technovation.services.BatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BatchServiceImpl implements BatchService {

    private BatchRepository batchRepository;

    @Override
    @Transactional
    public Batch save(Batch batch) {
        if (Objects.isNull(batch.getId())) {
            batch.setId(UUID.randomUUID());
        }
        return batchRepository.save(batch);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Batch> list() {
        return batchRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Batch> findById(UUID id) {
        return batchRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Batch> findByProgram(UUID programId) {
        Program program = new Program();

        program.setId(programId);

        return batchRepository.findByProgram(program);
    }

    @Override
    @Transactional
    public void registerUserToBatch(UUID batchId, UUID userId) {
        batchRepository.getUserByBatch(batchId, userId).ifPresentOrElse(
                user -> {
                    throw new UserAlreadyRegisteredInBatch(user.getId() + "has been registered already");
                }, ()->
        batchRepository.registerUserToBatch(batchId, userId));

    }

    @Autowired
    public void setBatchRepository(BatchRepository batchRepository) {
        this.batchRepository = batchRepository;
    }
}
