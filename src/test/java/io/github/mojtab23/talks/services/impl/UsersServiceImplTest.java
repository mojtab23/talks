package io.github.mojtab23.talks.services.impl;

import io.github.mojtab23.talks.domains.User;
import io.github.mojtab23.talks.domains.UserRole;
import io.github.mojtab23.talks.dtos.UserDto;
import io.github.mojtab23.talks.repositories.UsersRepository;
import io.github.mojtab23.talks.services.UsersService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsersServiceImplTest {

    @Mock
    UsersRepository userRepository;

    @Test
    void getAllUsers() {

        Set<UserRole> roles = new HashSet<>(Arrays.asList(UserRole.ADMIN, UserRole.ATTENDEE, UserRole.SPEAKER));
        final User u1 = new User("u_1", "Name1", roles);
        final User u2 = new User("u_2", "Name2", null);
        final User u3 = new User("u_3", "Name3", Collections.singleton(UserRole.ATTENDEE));

        final List<User> users = Arrays.asList(u1, u2, u3);

        when(userRepository.findAll()).thenReturn(users);

        final UsersService usersService = new UsersServiceImpl(userRepository);


        final List<UserDto> allUsers = usersService.getAllUsers();

        final UserDto ud1 = new UserDto("u_1", "Name1", roles);
        final UserDto ud2 = new UserDto("u_2", "Name2", null);
        final UserDto ud3 = new UserDto("u_3", "Name3", Collections.singleton(UserRole.ATTENDEE));


        assertIterableEquals(Arrays.asList(ud1, ud2, ud3), allUsers);

    }
}
