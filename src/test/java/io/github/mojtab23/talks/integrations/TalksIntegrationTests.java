package io.github.mojtab23.talks.integrations;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.mojtab23.talks.domains.Subscription;
import io.github.mojtab23.talks.domains.Talk;
import io.github.mojtab23.talks.domains.User;
import io.github.mojtab23.talks.dtos.RegisterTalkDto;
import io.github.mojtab23.talks.dtos.TalkDto;
import io.github.mojtab23.talks.helper.HelperPage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
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
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

public class TalksIntegrationTests {
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
    public void registerATalk() {
        final RegisterTalkDto talkDto = new RegisterTalkDto("test talk", "this is the test talk", "u_1",
                Instant.EPOCH.plus(2, ChronoUnit.DAYS), Duration.of(1, ChronoUnit.HOURS));
        final URI uri = restTemplate.postForLocation("http://localhost:" + port + "/talks/", talkDto);

        assertThat(uri).isNotNull();
        final ResponseEntity<TalkDto> entity = restTemplate.getForEntity(uri, TalkDto.class);

        assertThat(uri.toString()).contains("/talks/");
        assertThat(entity.getBody()).isNotNull();
        assertThat(entity.getBody().getSpeakerId()).isEqualTo("u_1");
        assertThat(entity.getBody().getPlanedStartTime()).isEqualTo(Instant.EPOCH.plus(2, ChronoUnit.DAYS));
    }

    @Test
    public void registerATalkWithAdminRole() {
        final RegisterTalkDto talkDto = new RegisterTalkDto("test talk", "this is the test talk", "u_4",
                Instant.EPOCH.plus(2, ChronoUnit.DAYS), Duration.of(1, ChronoUnit.HOURS));
        final URI uri = restTemplate.postForLocation("http://localhost:" + port + "/talks/", talkDto);

        assertThat(uri).isNotNull();
        final ResponseEntity<TalkDto> entity = restTemplate.getForEntity(uri, TalkDto.class);

        assertThat(uri.toString()).contains("/talks/");
        assertThat(entity.getBody()).isNotNull();
        assertThat(entity.getBody().getSpeakerId()).isEqualTo("u_4");
        assertThat(entity.getBody().getPlanedStartTime()).isEqualTo(Instant.EPOCH.plus(2, ChronoUnit.DAYS));
    }

    @Test
    public void registerATalkWithSpeakerRole() {
        final RegisterTalkDto talkDto = new RegisterTalkDto("test talk", "this is the test talk", "u_2",
                Instant.EPOCH.plus(2, ChronoUnit.DAYS), Duration.of(1, ChronoUnit.HOURS));
        final URI uri = restTemplate.postForLocation("http://localhost:" + port + "/talks/", talkDto);

        assertThat(uri).isNotNull();
        final ResponseEntity<TalkDto> entity = restTemplate.getForEntity(uri, TalkDto.class);

        assertThat(uri.toString()).contains("/talks/");
        assertThat(entity.getBody()).isNotNull();
        assertThat(entity.getBody().getSpeakerId()).isEqualTo("u_2");
        assertThat(entity.getBody().getPlanedStartTime()).isEqualTo(Instant.EPOCH.plus(2, ChronoUnit.DAYS));
    }

    @DisplayName("Register a talk will fail if user has not the roles 'admin' or 'speaker'")
    @Test
    public void registerTalkFailsIfUserHasNoRole() {
        final RegisterTalkDto talkDto = new RegisterTalkDto("test talk", "this is the test talk", "u_3",
                Instant.EPOCH.plus(10, ChronoUnit.MINUTES), Duration.of(1, ChronoUnit.HOURS));
        final ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:" + port + "/talks/", talkDto, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).contains("can't insert the talk");
    }

    @Test
    public void registerAnOverlappingTalkFails() {
        final RegisterTalkDto talkDto = new RegisterTalkDto("test talk", "this is the test talk", "u_1",
                Instant.EPOCH.plus(10, ChronoUnit.MINUTES), Duration.of(1, ChronoUnit.HOURS));
        final ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:" + port + "/talks/", talkDto, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).contains("can't insert the talk");
    }

    @Test
    void getTalksInADateRange() throws Exception {


        final ResponseEntity<HelperPage> entity = restTemplate.exchange(
                "http://localhost:" + port + "/talks?from={f}&to={t}",
                HttpMethod.GET,
                HttpEntity.EMPTY,
                HelperPage.class,
                "1970-01-01T00:00:00.000-00:00",
                "1970-01-02T00:00:00.000-00:00"
        );

        assertThat(entity.getStatusCodeValue()).isEqualTo(200);
        assertThat(entity.getBody()).isNotNull();
        assertThat(entity.getBody().getNumberOfElements()).isEqualTo(2);
        assertThat(entity.getBody().stream().map(TalkDto::getId).collect(Collectors.toList())).contains("t_1", "t_3");


    }

    @Test
    void searchTalks() {


        final ResponseEntity<TalkDto[]> entity = restTemplate.exchange(
                "http://localhost:" + port + "/talks/search?" +
                        "title=First&" +
                        "description=the&" +
                        "speakerId=u_1&" +
                        "planedStartFrom=1970-01-01T00:00:00.000-00:00&" +
                        "planedStartTo=1970-01-02T00:00:00.000-00:00&" +
                        "planedEndFrom=1970-01-01T00:10:00.000-00:00&" +
                        "planedEndTo=1970-01-01T00:40:00.000-00:00&" +
                        "startedFrom=2022-01-05T16:00:00.000-00:00&" +
                        "startedTo=2022-01-05T16:30:00.000-00:00&" +
                        "endedFrom=2022-01-05T16:30:00.000-00:00&" +
                        "endedTo=2022-01-05T17:00:00.000-00:00",
                HttpMethod.GET,
                HttpEntity.EMPTY,
                TalkDto[].class
        );

        assertThat(entity.getStatusCodeValue()).isEqualTo(200);
        assertThat(entity.getBody()).isNotNull();
        assertThat(entity.getBody().length).isEqualTo(1);
        assertThat(entity.getBody()[0].getId()).contains("t_1");


    }

    @Test
    void searchTalksFindNone() {


        final ResponseEntity<TalkDto[]> entity = restTemplate.exchange(
                "http://localhost:" + port + "/talks/search?" +
                        "title=First&" +
                        "description=the&" +
                        "speakerId=u_1&" +
                        "planedStartFrom=1970-01-01T00:00:00.000-00:00&" +
                        "planedStartTo=1970-01-02T00:00:00.000-00:00&" +
                        // -- Date range changed to 1971 so can't find the talk
                        "planedEndFrom=1971-01-01T00:10:00.000-00:00&" +
                        "planedEndTo=1971-01-01T00:40:00.000-00:00&" +
                        // --
                        "startedFrom=2022-01-05T16:00:00.000-00:00&" +
                        "startedTo=2022-01-05T16:30:00.000-00:00&" +
                        "endedFrom=2022-01-05T16:30:00.000-00:00&" +
                        "endedTo=2022-01-05T17:00:00.000-00:00",
                HttpMethod.GET,
                HttpEntity.EMPTY,
                TalkDto[].class
        );

        assertThat(entity.getStatusCodeValue()).isEqualTo(200);
        assertThat(entity.getBody()).isNotNull();
        assertThat(entity.getBody().length).isEqualTo(0);

    }

    @Test
    void getTalksBySpeaker() throws Exception {


        final ResponseEntity<HelperPage> entity = restTemplate.exchange(
                "http://localhost:" + port + "/talks/speaker/{id}",
                HttpMethod.GET,
                HttpEntity.EMPTY,
                HelperPage.class,
                "u_1"
        );

        assertThat(entity.getStatusCodeValue()).isEqualTo(200);
        assertThat(entity.getBody()).isNotNull();
        assertThat(entity.getBody().getNumberOfElements()).isEqualTo(2);
        assertThat(entity.getBody().stream().map(TalkDto::getId).collect(Collectors.toList())).contains("t_1", "t_3");


    }


    @AfterEach
    void tearDown() {
        mongoTemplate.dropCollection(Talk.class);
        mongoTemplate.dropCollection(User.class);
        mongoTemplate.remove(Subscription.class).all();
    }

}
