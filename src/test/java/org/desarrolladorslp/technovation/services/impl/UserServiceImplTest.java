package org.desarrolladorslp.technovation.services.impl;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.desarrolladorslp.technovation.models.Role;
import org.desarrolladorslp.technovation.models.User;
import org.desarrolladorslp.technovation.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Test
    public void givenAnExistentUserWithRoles_WhenActivate_ThenSuccess() {

        // Given:
        UserServiceImpl userService = new UserServiceImpl();

        userService.setUserRepository(userRepository);


        User user = User.builder()
                .id(UUID.randomUUID())
                .enabled(true)
                .roles(Stream.of("ROLE_ADMINISTRATOR", "ROLE_TECKER")
                        .map(roleName -> Role.builder().name(roleName).build())
                        .collect(Collectors.toList()))
                .build();

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        // When:
        userService.activate(user);

        // Then
        ArgumentCaptor<User> usedStoredUser = ArgumentCaptor.forClass(User.class);

        verify(userRepository).findById(user.getId());
        verify(userRepository).save(usedStoredUser.capture());
        verifyNoMoreInteractions(userRepository);

        User storedUserValue = usedStoredUser.getValue();

        assertThat(storedUserValue).isNotNull();
        assertThat(storedUserValue.getId()).isEqualTo(user.getId());
        assertThat(storedUserValue.getRoles()).isEqualTo(user.getRoles());
        assertThat(storedUserValue.isValidated()).isTrue();
        assertThat(storedUserValue.getName()).isNull();
        assertThat(storedUserValue.getPreferredEmail()).isNull();
        assertThat(storedUserValue.getPhoneNumber()).isNull();
        assertThat(storedUserValue.isEnabled()).isEqualTo(user.isEnabled());

    }

    @Test
    public void givenAnExistentUserWithEmptyRolesList_WhenActivate_ThenThrowException() {

        // Given:
        UserServiceImpl userService = new UserServiceImpl();

        userService.setUserRepository(userRepository);

        User user = User.builder()
                .id(UUID.randomUUID())
                .roles(Collections.emptyList())
                .enabled(false)
                .build();

        // Then:
        assertThatIllegalArgumentException().isThrownBy(() -> userService.activate(user));

        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void givenAnExistentUserWithRolesListBeingNull_WhenActivate_ThenThrowException() {

        // Given:
        UserServiceImpl userService = new UserServiceImpl();

        userService.setUserRepository(userRepository);

        User user = User.builder()
                .id(UUID.randomUUID())
                .enabled(false)
                .build();

        // When/Then:
        assertThatIllegalArgumentException().isThrownBy(() -> userService.activate(user));

        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void givenANonExistentUserWithRoles_WhenActivate_ThenThrowUsernameNotFoundException() {

        // Given:
        UserServiceImpl userService = new UserServiceImpl();

        userService.setUserRepository(userRepository);

        User user = User.builder()
                .id(UUID.randomUUID())
                .roles(Stream.of("ROLE_ADMINISTRATOR")
                        .map(roleName -> Role.builder().name(roleName).build())
                        .collect(Collectors.toList()))
                .enabled(false)
                .build();

        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        // Then:
        assertThatExceptionOfType(UsernameNotFoundException.class)
                .isThrownBy(() -> userService.activate(user));

        verify(userRepository).findById(user.getId());
        verifyNoMoreInteractions(userRepository);
    }

}
