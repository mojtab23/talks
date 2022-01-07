package io.github.mojtab23.talks.services.impl;

import io.github.mojtab23.talks.dtos.RegisterUserDto;
import io.github.mojtab23.talks.domains.User;
import io.github.mojtab23.talks.dtos.UserDto;
import io.github.mojtab23.talks.repositories.UsersRepository;
import io.github.mojtab23.talks.services.UsersService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public Optional<UserDto> getUser(String id) {
        return usersRepository.findById(id).map(UserDto::fromUser);
    }

    @Override
    public List<UserDto> getAllUsers() {
        final List<User> users = usersRepository.findAll();
        return users.stream().map(UserDto::fromUser).collect(Collectors.toList());
    }

}
