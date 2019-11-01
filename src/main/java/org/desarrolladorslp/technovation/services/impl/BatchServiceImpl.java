package org.desarrolladorslp.technovation.services.impl;

import java.util.*;
import java.util.stream.Collectors;

import org.desarrolladorslp.technovation.dto.RegisterToBatchDTO;
import org.desarrolladorslp.technovation.exception.BatchCannotBeDeletedException;
import org.desarrolladorslp.technovation.exception.BatchDoesNotExistException;
import org.desarrolladorslp.technovation.exception.UserAlreadyRegisteredInBatch;
import org.desarrolladorslp.technovation.models.Batch;
import org.desarrolladorslp.technovation.models.Program;
import org.hibernate.jdbc.BatchFailedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.desarrolladorslp.technovation.repository.BatchRepository;
import org.desarrolladorslp.technovation.repository.UserRepository;
import org.desarrolladorslp.technovation.services.BatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BatchServiceImpl implements BatchService {

    private static final Logger logger = LoggerFactory.getLogger(BatchServiceImpl.class);

    private BatchRepository batchRepository;

    private UserRepository userRepository;

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
    public void delete(UUID id) {
        Optional<Batch> optionalBatch = batchRepository.findById(id);

        optionalBatch.ifPresent(batch -> batchRepository.doesBatchHaveSessions(id).ifPresentOrElse(
                doHaveSessions -> {
                    throw new BatchCannotBeDeletedException(id + " the batch contain sessions, can't delete the batch");
                }, () -> batchRepository.delete(batch))
        );
    }

    @Override
    @Transactional
    public void registerUserToBatch(UUID batchId, UUID userId) {
        userRepository.getUserByBatch(batchId, userId).ifPresentOrElse(
                user -> {
                    throw new UserAlreadyRegisteredInBatch(user.getId() + " has been registered already");
                }, () ->
                        batchRepository.registerUserToBatch(batchId, userId));
    }


    @Override
    @Transactional
    public void registerMultipleUsersToBatch(RegisterToBatchDTO register) {

        Optional<Batch> optionalBatch = batchRepository.findById(register.getBatchId());

        optionalBatch.ifPresentOrElse(
                batch -> {
                    registerToBatch(register.getRegister(), batch.getId());
                    unregisterToBatch(register.getUnregister(), batch.getId());

                }, () -> {
                    throw new BatchDoesNotExistException("the batch doesn't exist");
                }
        );
    }

    @Override
    @Transactional
    public void registerToBatch(List<UUID> usersToRegister, UUID batchId) {
        List<UUID> errorToRegister = new ArrayList<>();

        logger.debug("Attempt to register users to batch {}", batchId);
        usersToRegister.forEach(
                idUser -> {
                    userRepository.findById(idUser).ifPresentOrElse(
                            user -> {
                                userRepository.getUserByBatch(batchId, user.getId()).ifPresentOrElse(
                                        u -> errorToRegister.add(u.getId()),
                                        () -> {
                                            batchRepository.registerUserToBatch(batchId, idUser);
                                            logger.info("registered to the batch {}", batchId);
                                        });
                            }, () -> errorToRegister.add(idUser)
                    );
                });

        if (!errorToRegister.isEmpty())
            logger.warn("error to register the users {}", errorToRegister);
    }

    @Override
    @Transactional
    public void unregisterToBatch(List<UUID> usersToUnregister, UUID batchId) {
        List<UUID> errorToUnregister = new ArrayList<>();

        logger.debug("Attempt to unregister users to batch {}", batchId);
        usersToUnregister.forEach(
                uuid -> {
                    userRepository.findById(uuid).ifPresentOrElse(
                            user -> {
                                batchRepository.unregisterUserToBatch(batchId, user.getId());
                                logger.info("registered to the batch {}", batchId);
                            }, () -> errorToUnregister.add(uuid)
                    );
                });

        if (!errorToUnregister.isEmpty())
            logger.warn("error to unregister the users {}", errorToUnregister);

    }

    @Autowired
    public void setBatchRepository(BatchRepository batchRepository) {
        this.batchRepository = batchRepository;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
