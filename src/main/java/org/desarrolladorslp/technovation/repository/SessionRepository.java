package org.desarrolladorslp.technovation.repository;

import java.util.List;
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

    @Query(value = "SELECT u.* FROM users AS u INNER JOIN confirm_attendance AS ca ON u.id = ca.user_id INNER JOIN users_roles AS ur ON u.id = ur.user_id WHERE ca.session_id = :id AND ur.role_name in('ROLE_STAFF','ROLE_MENTOR', 'ROLE_ADMINISTRATOR')", nativeQuery = true)
    List<User> staff(@Param("id") UUID sessionId);
}
