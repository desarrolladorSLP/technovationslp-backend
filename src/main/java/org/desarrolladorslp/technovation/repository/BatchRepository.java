package org.desarrolladorslp.technovation.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.desarrolladorslp.technovation.models.Batch;
import org.desarrolladorslp.technovation.models.Program;
import org.desarrolladorslp.technovation.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BatchRepository extends JpaRepository<Batch, UUID> {

    List<Batch> findByProgram(Program program);

    @Modifying
    @Query(value = "INSERT INTO users_by_batch (batch_id, user_id) VALUES (:batchId, :userId)", nativeQuery = true)
    void registerUserToBatch(UUID batchId, UUID userId);

    @Query("SELECT u FROM User u JOIN UserByBatch ub ON u.id = ub.userId WHERE ub.batchId = :batchId AND ub.userId =:userId ")
    Optional<User> getUserByBatch(UUID batchId, UUID userId);
}
