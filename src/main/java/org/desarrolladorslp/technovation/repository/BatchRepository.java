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

    @Query(value = "SELECT DISTINCT true FROM sessions WHERE  batch_id = :batchId", nativeQuery = true)
    Optional<Boolean> doesBatchHaveSessions(UUID batchId);

    @Modifying
    @Query(value = "INSERT INTO users_by_batch (batch_id, user_id) VALUES (:batchId, :userId)", nativeQuery = true)
    void registerUserToBatch(UUID batchId, UUID userId);

    @Modifying
    @Query(value = "DELETE FROM users_by_batch WHERE batch_id = :batchId AND user_id = :userId", nativeQuery = true)
    void unregisterUserToBatch(UUID batchId, UUID userId);

    @Query(value = "SELECT DISTINCT true FROM users_by_batch WHERE user_id = :userId AND batch_id = :batchId", nativeQuery = true)
    Optional<Boolean> areTeckerAndDeliverableAssignedInTheSameBatch(UUID userId, UUID batchId);

    @Query("SELECT u FROM UserByBatch ub JOIN UsersByRole ur ON ub.userId = ur.userId JOIN User u ON ur.userId = u.id WHERE ub.batchId = :batchId AND ur.roleName = 'ROLE_TECKER'")
    List<User> getTeckersByBatch(UUID batchId);

}
