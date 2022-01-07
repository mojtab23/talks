package io.github.mojtab23.talks.integrations;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.mojtab23.talks.domains.Subscription;
import io.github.mojtab23.talks.domains.Talk;
import io.github.mojtab23.talks.domains.User;
import io.github.mojtab23.talks.dtos.RegisterTalkDto;
import io.github.mojtab23.talks.dtos.TalkDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

public class SubscriptionsIntegrationTests {
    private static final File SAMPLE_USERS_JSON = Paths.get("src", "test", "resources", "data", "sample-users.json").toFile();
    private static final File SAMPLE_JSON = Paths.get("src", "test", "resources", "data", "sample-talks.json").toFile();
    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private ObjectMapper mapper;

    @BeforeEach
    void setUp() throws IOException {
        final User[] users = mapper.readValue(SAMPLE_USERS_JSON, User[].class);
        Arrays.stream(users).forEach(mongoTemplate::save);
        final Talk[] talks = mapper.readValue(SAMPLE_JSON, Talk[].class);
        Arrays.stream(talks).forEach(mongoTemplate::save);
    }


    @Test
    public void subscribeToATalk() {

        final Instant now = Instant.now();
        final String talkId = createANewTalk(now);

        final ResponseEntity<String> entity = restTemplate.postForEntity(
                "http://localhost:" + port + "/talks/" + talkId + "/subscribers",
                "u_3",
                String.class
        );
        assertThat(entity.getStatusCodeValue()).isEqualTo(200);
        assertThat(entity.getBody()).isNotBlank();
    }

    private String createANewTalk(Instant time) {
        final RegisterTalkDto talkDto = new RegisterTalkDto("test talk", "this is the test talk", "u_1",
                time.plus(2, ChronoUnit.DAYS), Duration.of(1, ChronoUnit.HOURS));
        final URI uri = restTemplate.postForLocation("http://localhost:" + port + "/talks/", talkDto);

        assertThat(uri).isNotNull();
        final ResponseEntity<TalkDto> entity = restTemplate.getForEntity(uri, TalkDto.class);

        assertThat(uri.toString()).contains("/talks/");
        assertThat(entity.getBody()).isNotNull();
        return entity.getBody().getId();
    }

    @Test
    public void speakerCantSubscribeToHisTalk() {
        final Instant now = Instant.now();
        final String talkId = createANewTalk(now);


        final ResponseEntity<String> entity = restTemplate.postForEntity(
                "http://localhost:" + port + "/talks/" + talkId + "/subscribers",
                "u_1",
                String.class
        );
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(entity.getBody()).isNotBlank();
    }

    @Test
    public void SubscribeToOutDatedTalkFails() {
        final ResponseEntity<String> entity = restTemplate.postForEntity(
                "http://localhost:" + port + "/talks/t_1/subscribers",
                "u_3",
                String.class
        );
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(entity.getBody()).isNotBlank();
    }


    @AfterEach
    void tearDown() {
        mongoTemplate.dropCollection(Talk.class);
        mongoTemplate.dropCollection(User.class);
        mongoTemplate.dropCollection(Subscription.class);
    }

}
