package io.github.mojtab23.talks.repositories;

import io.github.mojtab23.talks.domains.Talk;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TalksRepository extends MongoRepository<Talk, String> {
}