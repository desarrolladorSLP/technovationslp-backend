package org.desarrolladorslp.technovation.repository;

import org.desarrolladorslp.technovation.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TeckerByParentRepository extends JpaRepository<User, UUID> {

    @Query("SELECT DISTINCT true FROM UsersByRole WHERE userId = :parentId AND roleName IN ('ROLE_PARENT')")
    Optional<Boolean> isParent(UUID parentId);

    @Query("SELECT u FROM User u INNER JOIN TeckerByParent tp ON u.id = tp.teckerId WHERE tp.parentId = :parentId")
    List<User> getTeckersByParent(UUID parentId);

    @Modifying
    @Query(value = "DELETE FROM teckers_by_parents WHERE parent_id = :parentId", nativeQuery = true)
    void removeAssignment(UUID parentId);

    @Modifying
    @Query(value = "INSERT INTO teckers_by_parents(tecker_id, parent_id) VALUES(:teckerId, :parentId)", nativeQuery = true)
    void assignmentToParent(UUID parentId, UUID teckerId);
}
