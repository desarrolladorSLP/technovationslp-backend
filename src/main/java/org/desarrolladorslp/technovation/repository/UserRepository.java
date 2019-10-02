package org.desarrolladorslp.technovation.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.desarrolladorslp.technovation.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    List<User> findByValidated(boolean isValidated);

    @Query("SELECT u FROM User u INNER JOIN MessagesByUser mbu ON u.id = mbu.userReceiverId WHERE mbu.messageId = :messageId")
    List<User> getUsersReceiversToSpecificMessage(UUID messageId);

    @Query("SELECT u FROM User u JOIN UsersByRole ur ON u.id = ur.userId WHERE ur.roleName = :roleName")
    List<User> getUsersByRole(String roleName);

    @Query("SELECT u FROM User u JOIN UserBySession us ON u.id = us.userId WHERE us.sessionId = :sessionId AND us.userId = :userId")
    Optional<User> getUserBySession(UUID sessionId, UUID userId);

    @Query("SELECT u FROM User u JOIN UserBySession us ON u.id = us.userId WHERE us.sessionId = :id")
    List<User> allPeopleBySession(@Param("id") UUID sessionId);

    @Query(value = "SELECT u FROM User u JOIN UserBySession ca ON u.id = ca.userId JOIN UsersByRole ur ON u.id = ur.userId WHERE ca.sessionId = :id AND ur.roleName in('ROLE_STAFF','ROLE_MENTOR', 'ROLE_ADMINISTRATOR')")
    List<User> staffBySession(@Param("id") UUID sessionId);

    @Query("SELECT u FROM User u JOIN UserByBatch ub ON u.id = ub.userId WHERE ub.batchId = :batchId AND ub.userId =:userId ")
    Optional<User> getUserByBatch(UUID batchId, UUID userId);
}
