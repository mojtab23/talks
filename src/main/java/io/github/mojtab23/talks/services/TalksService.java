package io.github.mojtab23.talks.services;

import io.github.mojtab23.talks.dtos.TalkDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TalksService {

    Page<TalkDto> getAllTalks(Pageable pageable);

    Page<TalkDto> getTalksBySpeakerId(String speakerId, Pageable pageable);
}
