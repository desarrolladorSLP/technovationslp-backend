package org.desarrolladorslp.technovation.services;

import org.desarrolladorslp.technovation.models.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface IUserService extends UserDetailsService {
    User findByUsername(String username);

    UserDetails tryToRegister(User user, String... roleNames);
}
