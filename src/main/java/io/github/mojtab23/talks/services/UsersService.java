package io.github.mojtab23.talks.services;

import io.github.mojtab23.talks.dtos.RegisterUserDto;
import io.github.mojtab23.talks.dtos.UserDto;

import java.util.List;
import java.util.Optional;

public interface UsersService {

    String registerUser(RegisterUserDto dto);
    Optional<UserDto> getUser(String  id);

    List<UserDto> getAllUsers();
}
