package io.github.mojtab32.talks.repositories;

import io.github.mojtab32.talks.domains.Talk;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TalksRepository extends MongoRepository<Talk, String> {
}
