package io.github.mojtab32.talks.repositories;

import io.github.mojtab32.talks.domains.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UsersRepository extends MongoRepository<User, String> {
}
