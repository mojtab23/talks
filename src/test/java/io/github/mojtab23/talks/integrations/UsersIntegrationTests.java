package io.github.mojtab23.talks.integrations;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.mojtab23.talks.domains.Subscription;
import io.github.mojtab23.talks.domains.Talk;
import io.github.mojtab23.talks.domains.User;
import io.github.mojtab23.talks.domains.UserRole;
import io.github.mojtab23.talks.dtos.RegisterUserDto;
import io.github.mojtab23.talks.dtos.UserDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.ResponseEntity;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

public class UsersIntegrationTests {
    private static final File SAMPLE_USERS_JSON = Paths.get("src", "test", "resources", "data", "sample-users.json").toFile();
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
    }


    @Test
    public void registerAUser() {
        final RegisterUserDto userDto = new RegisterUserDto("test user", Collections.singleton(UserRole.ATTENDEE));
        final URI uri = restTemplate.postForLocation("http://localhost:" + port + "/users/", userDto);

        assertThat(uri).isNotNull();
        final ResponseEntity<UserDto> entity = restTemplate.getForEntity(uri, UserDto.class);

        assertThat(uri.toString()).contains("/users/");
        assertThat(entity.getBody()).isNotNull();
        assertThat(entity.getBody().getId()).isNotBlank();
        assertThat(entity.getBody().getName()).isEqualTo("test user");
        assertThat(entity.getBody().getRoles()).hasSize(1);
    }


    @AfterEach
    void tearDown() {
        mongoTemplate.dropCollection(Talk.class);
        mongoTemplate.dropCollection(User.class);
        mongoTemplate.remove(Subscription.class).all();
    }

}
