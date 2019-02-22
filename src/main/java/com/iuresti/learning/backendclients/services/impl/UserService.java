package com.iuresti.learning.backendclients.services.impl;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.iuresti.learning.backendclients.models.User;
import com.iuresti.learning.backendclients.repository.MemoryUserRepository;
import com.iuresti.learning.backendclients.services.IUserService;

@Service
public class UserService implements IUserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private MemoryUserRepository userDAO;


    public UserService(MemoryUserRepository userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userDAO.findByUsername(username);

        if (user == null) {
            logger.warn("Username with id {} was not found", username);
            throw new UsernameNotFoundException("User doesn't exist: " + username);
        }

        return buildUserDetails(user);
    }

    @Override
    public User findByUsername(String username) {
        return userDAO.findByUsername(username);
    }

    @Override
    public UserDetails tryToRegister(User user, String... roleNames) {

        user.setRoles(Arrays.asList(roleNames));
        userDAO.save(user);

        return buildUserDetails(user);
    }

    private UserDetails buildUserDetails(User user) {
        List<GrantedAuthority> authorities = user.getRoles()
                .stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());

        return new org.springframework.security.core.userdetails.User(user.getUsername(), "", authorities);
    }
}
