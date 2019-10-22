package org.desarrolladorslp.technovation.config.auth;

import java.util.UUID;

import org.desarrolladorslp.technovation.models.FirebaseUser;
import org.desarrolladorslp.technovation.models.User;
import org.desarrolladorslp.technovation.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class TechnovationSlpAuthenticationManager implements AuthenticationManager {

    private static final Logger logger = LoggerFactory.getLogger(TechnovationSlpAuthenticationManager.class);

    private UserService userService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UserDetails userDetails;

        try {
            logger.debug("Authenticating user");
            userDetails = userService.loadUserByUsername(authentication.getName());
            logger.info("Existent user {}", authentication.getName());
        } catch (UsernameNotFoundException ex) {
            logger.info("Non Existent user {}, creating registry", authentication.getName());
            userDetails = tryToRegister((TokenInfo) authentication.getDetails());
            logger.info("New user registered");
        }

        return new TechnovationSlpAuthenticationToken(authentication, userDetails.getAuthorities(), true);
    }

    private UserDetails tryToRegister(TokenInfo details) {

        User user = User.builder()
                .id(UUID.randomUUID())
                .name(details.getName())
                .preferredEmail(details.getEmail())
                .pictureUrl(details.getPictureUrl())
                .enabled(false)
                .validated(false)
                .build();

        FirebaseUser firebaseUser = FirebaseUser.builder()
                .uid(details.getUid())
                .email(details.getEmail())
                .user(user)
                .build();

        return userService.register(firebaseUser);
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
