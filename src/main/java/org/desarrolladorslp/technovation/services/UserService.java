package org.desarrolladorslp.technovation.services;

import java.util.Optional;

import org.desarrolladorslp.technovation.models.FirebaseUser;
import org.desarrolladorslp.technovation.models.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    Optional<User> findByUsername(String username);

    UserDetails register(FirebaseUser firebaseUser);
}
