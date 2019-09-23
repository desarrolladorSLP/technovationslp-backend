package org.desarrolladorslp.technovation.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.desarrolladorslp.technovation.dto.UsersByRoleDTO;
import org.desarrolladorslp.technovation.models.FirebaseUser;
import org.desarrolladorslp.technovation.models.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    Optional<User> findByUsername(String username);

    UserDetails register(FirebaseUser firebaseUser);

    List<User> findByValidated(boolean b);

    User activate(User user);

    List<User> findAll();

    User findById(UUID uuid);

    User save(User user);

    List<UsersByRoleDTO> getUsersByRole(String roleName);
}
