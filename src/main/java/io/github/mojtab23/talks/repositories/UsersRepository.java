package io.github.mojtab23.talks.repositories;

import io.github.mojtab23.talks.domains.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UsersRepository extends MongoRepository<User, String> {
}
