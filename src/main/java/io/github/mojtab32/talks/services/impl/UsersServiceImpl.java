package io.github.mojtab32.talks.services.impl;

import io.github.mojtab32.talks.domains.User;
import io.github.mojtab32.talks.dtos.RegisterUserDto;
import io.github.mojtab32.talks.dtos.UserDto;
import io.github.mojtab32.talks.repositories.UsersRepository;
import io.github.mojtab32.talks.services.UsersService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class UsersServiceImpl implements UsersService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final UsersRepository usersRepository;

    public UsersServiceImpl(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public String registerUser(RegisterUserDto dto) {
        try {
            final User savedUser = usersRepository.save(dto.toUser());
            return savedUser.getId();
        } catch (Exception e) {
            e.printStackTrace();
            log.warn("error in saving a user",e);
        }
        return null;
    }

    @Override
    public UserDto getUser(String id) {
        return null;
    }

}
