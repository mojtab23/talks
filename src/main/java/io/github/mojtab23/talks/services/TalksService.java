package io.github.mojtab23.talks.services;

import io.github.mojtab23.talks.dtos.RegisterTalkDto;
import io.github.mojtab23.talks.dtos.TalkDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface TalksService {

    Page<TalkDto> getAllTalks(Instant from, Instant to, Pageable pageable);

    Page<TalkDto> getTalksBySpeakerId(String speakerId, Pageable pageable);

    String registerTalk(RegisterTalkDto dto);

    Optional<TalkDto> getTalkById(String id);

    String subscribeToTalk(String talkId, String attendeeId);

    List<TalkDto> searchTalks(
            String title,
            String description,
            String speakerId,
            Date planedStartFrom,
            Date planedStartTo,
            Date planedEndFrom,
            Date planedEndTo,
            Date startedFrom,
            Date startedTo,
            Date endedFrom,
            Date endedTo
    );
}
