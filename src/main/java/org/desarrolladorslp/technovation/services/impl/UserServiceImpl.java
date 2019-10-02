package org.desarrolladorslp.technovation.services.impl;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.desarrolladorslp.technovation.dto.UsersByRoleDTO;
import org.desarrolladorslp.technovation.models.FirebaseUser;
import org.desarrolladorslp.technovation.models.User;
import org.desarrolladorslp.technovation.repository.FirebaseUserRepository;
import org.desarrolladorslp.technovation.repository.UserRepository;
import org.desarrolladorslp.technovation.services.UserService;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private UserRepository userRepository;

    private FirebaseUserRepository firebaseUserRepository;

    private ModelMapper modelMapper;

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

        FirebaseUser existentFirebaseUser = firebaseUserRepository.findByEmail(firebaseUser.getEmail());

        User user = Objects.isNull(existentFirebaseUser) ? firebaseUser.getUser() : existentFirebaseUser.getUser();

        userRepository.save(user);
        firebaseUser.setUser(user);
        firebaseUserRepository.save(firebaseUser);

        return buildUserDetails(user);
    }

    @Override
    public List<User> findByValidated(boolean isValidated) {
        return userRepository.findByValidated(isValidated);
    }

    @Override
    public User activate(User user) {

        if (CollectionUtils.isEmpty(user.getRoles()) || user.getId() == null) {
            throw new IllegalArgumentException("At least one role is required");
        }

        User storedUser = userRepository.findById(user.getId()).orElseThrow(() -> {
            logger.warn("Username with id {} was not found", user.getId());

            return new UsernameNotFoundException("User doesn't exist: " + user.getId());
        });

        storedUser.setValidated(true);
        storedUser.setEnabled(user.isEnabled());

        storedUser.setRoles(user.getRoles());

        return userRepository.save(storedUser);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll(Sort.by("name"));
    }

    @Override
    public User findById(UUID uuid) {
        return userRepository.findById(uuid).orElseThrow();
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    private UserDetails buildUserDetails(User user) {
        List<GrantedAuthority> authorities = CollectionUtils.isEmpty(user.getRoles()) ?
                Collections.emptyList() :
                user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());

        return new org.springframework.security.core.userdetails.User(user.getName(), "", authorities);
    }

    @Override
    public List<UsersByRoleDTO> getUsersByRole(String roleName) {
        List<User> users = userRepository.getUsersByRole(roleName);
        return users.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @PostConstruct
    public void prepareMappings() {
        modelMapper.addMappings(new PropertyMap<User, UsersByRoleDTO>() {
            @Override
            protected void configure() {
                map().setId(source.getId());
                map().setName(source.getName());
            }
        });
    }

    private UsersByRoleDTO convertToDTO(User user) {
        return modelMapper.map(user, UsersByRoleDTO.class);
    }

    @Autowired
    public void setModelMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
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
