package io.github.mojtab32.talks.controllers;

import io.github.mojtab32.talks.domains.Talk;
import io.github.mojtab32.talks.services.TalksService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(SpringExtension.class)
@WebMvcTest
class TalksControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TalksService talksService;

    @Test
    void getAllTalks() throws Exception {
        List<Talk> talkList = new ArrayList<>();

        final Talk firstTalk = new Talk("First Talk", "This is the First Talk!", "1",
                Instant.EPOCH, Duration.of(30, ChronoUnit.MINUTES)
        );
        firstTalk.setId("t_1");
        talkList.add(firstTalk);
        final Talk secondTalk = new Talk("Second Talk", "This is the Second Talk!", "2",
                Instant.EPOCH.plus(1, ChronoUnit.DAYS), Duration.of(1, ChronoUnit.HOURS)
        );
        secondTalk.setId("t_2");
        talkList.add(secondTalk);
        when(talksService.getAllTalks()).thenReturn(talkList);

        mockMvc.perform(MockMvcRequestBuilders.get("/talks")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(jsonPath("$", hasSize(2))).andDo(print());
    }
}
