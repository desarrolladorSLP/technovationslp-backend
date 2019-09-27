package org.desarrolladorslp.technovation.repository;

import java.util.List;
import java.util.UUID;

import org.desarrolladorslp.technovation.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    List<User> findByValidated(boolean isValidated);

    @Query("SELECT u FROM User u JOIN UsersByRole ur ON u.id = ur.userId WHERE ur.roleName = :roleName")
    List<User> getUsersByRole(String roleName);
}
