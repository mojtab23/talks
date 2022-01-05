package io.github.mojtab32.talks.services;

import io.github.mojtab32.talks.dtos.RegisterUserDto;
import io.github.mojtab32.talks.dtos.UserDto;

public interface UsersService {

    String registerUser(RegisterUserDto dto);
    UserDto getUser(String  id);

}
