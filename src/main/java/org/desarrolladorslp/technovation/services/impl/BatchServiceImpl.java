package org.desarrolladorslp.technovation.services.impl;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import org.desarrolladorslp.technovation.exception.BatchCannotBeDeletedException;
import org.desarrolladorslp.technovation.exception.UserAlreadyRegisteredInBatch;
import org.desarrolladorslp.technovation.models.Batch;
import org.desarrolladorslp.technovation.models.Program;
import org.desarrolladorslp.technovation.repository.BatchRepository;
import org.desarrolladorslp.technovation.repository.UserRepository;
import org.desarrolladorslp.technovation.services.BatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.validation.ConstraintViolationException;

@Service
public class BatchServiceImpl implements BatchService {

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
    public void registerToBatch(List<UUID> usersToRegister, UUID batchId) {
        List<UUID> errorToRegister = new ArrayList<>();
        usersToRegister.forEach(
                uuid -> {
                    userRepository.findById(uuid).ifPresentOrElse(
                            user -> {
                                userRepository.getUserByBatch(batchId,user.getId()).ifPresentOrElse(
                                    u -> errorToRegister.add(u.getId()), () -> batchRepository.registerUserToBatch(batchId,uuid));
                            }, () -> errorToRegister.add(uuid)
                    );
                });
        System.out.println("Error al registrar los siguientes teckers " + errorToRegister.stream().collect(Collectors.toList()));
    }

    @Override
    @Transactional
    public void unregisterToBatch(List<UUID> usersToUnregister, UUID batchId){
        List<UUID> errorToUnregister = new ArrayList<>();
        usersToUnregister.forEach(
                uuid -> {
                    userRepository.findById(uuid).ifPresentOrElse(
                            user -> {
                                batchRepository.unregisterUserToBatch(batchId,user.getId());
                            }, () -> errorToUnregister.add(uuid)
                    );
                });
        System.out.println("Error al eliminar del registro los siguientes teckers " + errorToUnregister.stream().collect(Collectors.toList()));
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
