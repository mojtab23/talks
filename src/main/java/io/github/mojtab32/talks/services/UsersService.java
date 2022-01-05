package io.github.mojtab32.talks.services;

import io.github.mojtab32.talks.dtos.RegisterUserDto;

public interface UsersService {

    String registerUser(RegisterUserDto dto);

}
