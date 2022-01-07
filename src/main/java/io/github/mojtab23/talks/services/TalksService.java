package io.github.mojtab23.talks.services;

import io.github.mojtab23.talks.dtos.RegisterTalkDto;
import io.github.mojtab23.talks.dtos.TalkDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.Optional;

public interface TalksService {

    Page<TalkDto> getAllTalks(Instant from, Instant to, Pageable pageable);

    Page<TalkDto> getTalksBySpeakerId(String speakerId, Pageable pageable);

    String registerTalk(RegisterTalkDto dto);

    Optional<TalkDto> getTalkById(String id);

    String subscribeToTalk(String talkId, String attendeeId);
}
