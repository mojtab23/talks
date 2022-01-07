package io.github.mojtab23.talks.controllers;

import io.github.mojtab23.talks.dtos.TalkDto;
import io.github.mojtab23.talks.services.TalksService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

//@ExtendWith(SpringExtension.class)
@WebMvcTest(TalksController.class)
class TalksControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TalksService talksService;

    @Test
    void getAllTalks() throws Exception {
        List<TalkDto> talkList = new ArrayList<>();

        final TalkDto firstTalk = new TalkDto("First Talk", "This is the First Talk!", "1",
                Instant.EPOCH, Instant.EPOCH.plus(30, ChronoUnit.MINUTES)
        );
        firstTalk.setId("t_1");
        talkList.add(firstTalk);
        final TalkDto secondTalk = new TalkDto("Second Talk", "This is the Second Talk!", "2",
                Instant.EPOCH.plus(1, ChronoUnit.DAYS), Instant.EPOCH.plus(1, ChronoUnit.HOURS)
        );
        secondTalk.setId("t_2");
        talkList.add(secondTalk);

        when(talksService.getAllTalks(any(), any(), any())).thenReturn(new PageImpl<>(talkList));

        mockMvc.perform(MockMvcRequestBuilders.get("/talks")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "id,desc") // <-- no space after comma!!!
//                        .param("sort", "name,asc")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(jsonPath("$.content", hasSize(2))).andDo(print());

        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(talksService).getAllTalks(any(), any(), pageableCaptor.capture());
        PageRequest pageable = (PageRequest) pageableCaptor.getValue();

        assertEquals(pageable.getPageNumber(), 0);
        assertEquals(pageable.getPageSize(), 10);
        assertEquals("id: DESC", pageable.getSort().toString());

    }
}
