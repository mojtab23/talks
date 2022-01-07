package io.github.mojtab23.talks;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.mojtab23.talks.domains.Talk;
import io.github.mojtab23.talks.domains.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;

@Component
public class DataInitializer implements ApplicationListener<ContextRefreshedEvent> {

    private static final File SAMPLE_USERS_JSON = Paths.get("src", "main", "resources", "data", "sample-users.json").toFile();
    private static final File SAMPLE_JSON = Paths.get("src", "main", "resources", "data", "sample-talks.json").toFile();
    private final MongoTemplate mongoTemplate;
    private final ObjectMapper mapper;
    private final Logger log = LoggerFactory.getLogger(getClass());

    private final Boolean initialData;
    private boolean alreadySetup = false;

    public DataInitializer(
            MongoTemplate mongoTemplate,
            ObjectMapper mapper,
            @Value("${talks.initial-data}") Boolean initialData
    ) {
        this.mongoTemplate = mongoTemplate;
        this.mapper = mapper;
        this.initialData = initialData;
    }

    @Transactional
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

        if (alreadySetup) {
            return;
        }

        if (!initialData) {
            log.info("Skipping DataInitializer");
        }

        try {
            final User[] users = mapper.readValue(SAMPLE_USERS_JSON, User[].class);
            Arrays.stream(users).forEach(mongoTemplate::save);
            final Talk[] talks = mapper.readValue(SAMPLE_JSON, Talk[].class);
            Arrays.stream(talks).forEach(mongoTemplate::save);
        } catch (IOException e) {
            log.error("error in DataInitializer", e);
        }
        alreadySetup = true;
    }
}
