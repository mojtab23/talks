package io.github.mojtab32.talks.repositories;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.mojtab32.talks.domains.Talk;
import io.github.mojtab32.talks.services.TalksService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class TalksRepositoryTest {
    private static final File SAMPLE_JSON = Paths.get("src", "test", "resources", "data", "sample.json").toFile();

    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private TalksRepository talksRepository;

    @MockBean
    private TalksService talksService;

    @BeforeEach
    void setUp() throws IOException {

        final Talk[] talks = mapper.readValue(SAMPLE_JSON, Talk[].class);
        Arrays.stream(talks).forEach(mongoTemplate::save);
    }

    @AfterEach
    void tearDown() {
        mongoTemplate.dropCollection(Talk.class);
    }

    @Test
    void testFindAll() {
        final List<Talk> talks = talksRepository.findAll();
        assertEquals(2, talks.size(), "Should be two talks in the database");
    }

}
