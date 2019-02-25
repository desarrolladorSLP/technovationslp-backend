package org.desarrolladorslp.technovation.repository;

import org.desarrolladorslp.technovation.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
}
