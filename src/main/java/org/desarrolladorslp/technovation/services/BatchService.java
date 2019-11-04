package org.desarrolladorslp.technovation.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.desarrolladorslp.technovation.dto.MentorDTO;
import org.desarrolladorslp.technovation.dto.RegisterToBatchDTO;
import org.desarrolladorslp.technovation.models.Batch;

public interface BatchService {

    Batch save(Batch batch);

    List<Batch> list();

    Optional<Batch> findById(UUID id);

    List<Batch> findByProgram(UUID programId);

    void delete(UUID id);

    void registerUserToBatch(UUID batchId, UUID userId);

    void registerToBatch(List<UUID> usersToRegister, UUID batchId);

    void unregisterToBatch(List<UUID> usersToUnregister, UUID batchId);

    void registerMultipleUsersToBatch(RegisterToBatchDTO register);

    List<MentorDTO> getMentorsByBatch(UUID batchId);

}
