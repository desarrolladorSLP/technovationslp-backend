package org.desarrolladorslp.technovation.config.auth;

import java.util.List;

import org.desarrolladorslp.technovation.models.User;
import org.desarrolladorslp.technovation.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class FirebaseAuthenticationManager implements AuthenticationManager {

    private IUserService userService;

    @Value("${authentication.whitelist}")
    private List<String> whitelist;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UserDetails userDetails;
        try {
            userDetails = userService.loadUserByUsername(authentication.getName());
        } catch (UsernameNotFoundException ex) {
            userDetails = tryToRegister((FirebaseTokenHolder) authentication.getDetails());
        }

        return new FirebaseAuthenticationToken(authentication, userDetails.getAuthorities(), true);
    }

    private UserDetails tryToRegister(FirebaseTokenHolder details) {

        validateEmail(details.getEmail());

        User user = new User();

        user.setUsername(details.getUid());
        user.setEmail(details.getEmail());
        user.setEnabled(true);
        user.setName(details.getName());

        return userService.tryToRegister(user, "ROLE_TECKER", "ROLE_USER");
    }

    private void validateEmail(String email) {
        if (!whitelist.contains(email)) {
            throw new UsernameNotFoundException("User not allowed");
        }
    }

    @Autowired
    public void setUserService(IUserService userService) {
        this.userService = userService;
    }
}
