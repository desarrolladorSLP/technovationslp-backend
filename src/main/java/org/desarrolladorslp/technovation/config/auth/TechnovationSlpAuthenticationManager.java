package org.desarrolladorslp.technovation.config.auth;

import java.util.UUID;

import org.desarrolladorslp.technovation.models.FirebaseUser;
import org.desarrolladorslp.technovation.models.User;
import org.desarrolladorslp.technovation.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class TechnovationSlpAuthenticationManager implements AuthenticationManager {

    private IUserService userService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UserDetails userDetails;
        try {
            userDetails = userService.loadUserByUsername(authentication.getName());
        } catch (UsernameNotFoundException ex) {
            userDetails = tryToRegister((TokenInfo) authentication.getDetails());
        }

        return new TechnovationSlpAuthenticationToken(authentication, userDetails.getAuthorities(), true);
    }

    private UserDetails tryToRegister(TokenInfo details) {

        User user = new User();

        user.setId(UUID.randomUUID());
        user.setName(details.getName());
        user.setPreferredEmail(details.getEmail());
        user.setEnabled(false);
        user.setValidated(false);

        FirebaseUser firebaseUser = new FirebaseUser();

        firebaseUser.setUid(details.getUid());
        firebaseUser.setEmail(details.getEmail());
        firebaseUser.setUser(user);

        return userService.register(firebaseUser);
    }

    @Autowired
    public void setUserService(IUserService userService) {
        this.userService = userService;
    }
}
