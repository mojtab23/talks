package io.github.mojtab23.talks.repositories;

import io.github.mojtab23.talks.domains.Subscription;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SubscriptionsRepository extends MongoRepository<Subscription, String> {
}
