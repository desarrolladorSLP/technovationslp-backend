package org.desarrolladorslp.technovation.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.desarrolladorslp.technovation.models.FirebaseUser;
import org.desarrolladorslp.technovation.models.User;
import org.desarrolladorslp.technovation.repository.FirebaseUserRepository;
import org.desarrolladorslp.technovation.repository.UserRepository;
import org.desarrolladorslp.technovation.services.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService implements IUserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private UserRepository userRepository;

    private FirebaseUserRepository firebaseUserRepository;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        FirebaseUser firebaseUser = firebaseUserRepository.findById(username).orElseThrow(() -> {
            logger.warn("Username with id {} was not found", username);
            return new UsernameNotFoundException("User doesn't exist: " + username);
        });

        return buildUserDetails(firebaseUser.getUser());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Optional<User> findByUsername(String username) {
        Optional<FirebaseUser> firebaseUser = firebaseUserRepository.findById(username);

        return firebaseUser.map(FirebaseUser::getUser);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public UserDetails register(FirebaseUser firebaseUser) {

        User user = firebaseUser.getUser();

        userRepository.save(user);

        firebaseUserRepository.save(firebaseUser);

        return buildUserDetails(user);
    }

    private UserDetails buildUserDetails(User user) {
        List<GrantedAuthority> authorities = user.getRoles()
                .stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());

        return new org.springframework.security.core.userdetails.User(user.getName(), "", authorities);
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setFirebaseUserRepository(FirebaseUserRepository firebaseUserRepository) {
        this.firebaseUserRepository = firebaseUserRepository;
    }
}
