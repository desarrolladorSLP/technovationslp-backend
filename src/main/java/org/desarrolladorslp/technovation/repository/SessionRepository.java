package org.desarrolladorslp.technovation.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.desarrolladorslp.technovation.models.Batch;
import org.desarrolladorslp.technovation.models.Session;
import org.desarrolladorslp.technovation.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionRepository extends JpaRepository<Session, UUID> {

    List<Session> findByBatch(Batch batch);

    @Modifying
    @Query(value = "INSERT INTO confirm_attendance (session_id, user_id) VALUES (:sessionId, :userId)", nativeQuery = true)
    void confirmAttendance(UUID sessionId, UUID userId);

    @Query("SELECT u FROM User u JOIN UserBySession us ON u.id = us.userId WHERE us.sessionId = :id")
    List<User> allPeople(@Param("id") UUID sessionId);

    @Query("SELECT u FROM User u JOIN UserBySession us ON u.id = us.userId WHERE us.sessionId = :sessionId AND us.userId = :userId")
    Optional<User> getUserBySession(UUID sessionId, UUID userId);

    @Query(value = "SELECT u FROM User u JOIN UserBySession ca ON u.id = ca.userId JOIN UsersByRole ur ON u.id = ur.userId WHERE ca.sessionId = :id AND ur.roleName in('ROLE_STAFF','ROLE_MENTOR', 'ROLE_ADMINISTRATOR')")
    List<User> staff(@Param("id") UUID sessionId);

    @Query("SELECT s FROM Session s JOIN UserByBatch ub ON s.batch.id = ub.batchId JOIN User u ON ub.userId = u.id WHERE u.id = :userId ")
    List<Session> getSessionsByUser(UUID userId);
}
