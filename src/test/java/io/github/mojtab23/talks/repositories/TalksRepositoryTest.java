package io.github.mojtab23.talks.repositories;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.mojtab23.talks.domains.Subscription;
import io.github.mojtab23.talks.domains.Talk;
import io.github.mojtab23.talks.domains.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class TalksRepositoryTest {
    private static final File SAMPLE_JSON = Paths.get("src", "test", "resources", "data", "sample-talks.json").toFile();

    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private TalksRepository talksRepository;


    @BeforeEach
    void setUp() throws IOException {

        final Talk[] talks = mapper.readValue(SAMPLE_JSON, Talk[].class);
        Arrays.stream(talks).forEach(mongoTemplate::save);
    }

    @AfterEach
    void tearDown() {
        mongoTemplate.dropCollection(Talk.class);
        mongoTemplate.dropCollection(User.class);
        mongoTemplate.remove(Subscription.class).all();
    }

    @Test
    void testFindAll() {
        final List<Talk> talks = talksRepository.findAll();
        assertEquals(3, talks.size(), "Should be 3 talks in the database");
    }

    @Test
    void testFindTalksBetweenDates() {
        final Page<Talk> talks = talksRepository.findTalksBetween(Instant.EPOCH, Instant.EPOCH.plus(1, ChronoUnit.HOURS), PageRequest.of(0, 10));
        assertEquals(1, talks.getNumberOfElements(), "Should be one talk in the database");
        assertEquals("t_1", talks.getContent().get(0).getId());
    }

    @Test
    void testSpeakerHasOverlappingTalks() {
        final Integer count = talksRepository.countSpeakersOverlapingTalks("u_1", Instant.EPOCH, Instant.EPOCH.plus(1, ChronoUnit.DAYS));
        assertEquals(2, count, "Should be 2 overlapping talk");
    }

}
