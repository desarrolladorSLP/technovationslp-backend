package com.iuresti.learning.backendclients.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.iuresti.learning.backendclients.models.User;

public interface IUserService extends UserDetailsService {
    User findByUsername(String username);

    UserDetails tryToRegister(User user, String ... roleNames);
}
