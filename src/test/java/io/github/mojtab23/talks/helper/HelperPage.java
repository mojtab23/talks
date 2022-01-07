package io.github.mojtab23.talks.helper;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import io.github.mojtab23.talks.dtos.TalkDto;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class HelperPage extends PageImpl<TalkDto> {

    @JsonCreator
    public HelperPage(@JsonProperty("content") List<TalkDto> content,
                      @JsonProperty("number") int number,
                      @JsonProperty("size") int size,
                      @JsonProperty("pageable") JsonNode pageable,
                      @JsonProperty("totalElements") Long totalElements) {
        super(content, PageRequest.of(number, size), totalElements);
    }
}
