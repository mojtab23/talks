package io.github.mojtab23.talks.services.impl;

import io.github.mojtab23.talks.domains.Talk;
import io.github.mojtab23.talks.dtos.RegisterTalkDto;
import io.github.mojtab23.talks.dtos.TalkDto;
import io.github.mojtab23.talks.repositories.TalksRepository;
import io.github.mojtab23.talks.services.TalksService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class TalksServiceImpl implements TalksService {

    private final TalksRepository talksRepository;

    public TalksServiceImpl(TalksRepository talksRepository) {
        this.talksRepository = talksRepository;
    }

    @Override
    public Page<TalkDto> getAllTalks(Pageable pageable) {
        return talksRepository.findAll(pageable).map(TalkDto::fromTalk);
    }

    @Override
    public Page<TalkDto> getTalksBySpeakerId(String speakerId, Pageable pageable) {
        return talksRepository.findAllBySpeakerId(speakerId, pageable).map(TalkDto::fromTalk);
    }

    @Transactional
    @Override
    public String registerTalk(RegisterTalkDto dto) {
        final Talk talk = dto.toTalk();
        final Integer overlapingTalks = talksRepository.countSpeakersOverlapingTalks(talk.getSpeakerId(), talk.getPlanedStartTime(), talk.getPlanedEndTime());
        if (overlapingTalks > 0) {
            return null;
        }
        final Talk savedTalk = talksRepository.save(talk);
        return savedTalk.getId();
    }

    @Override
    public Optional<TalkDto> getTalkById(String id) {
        final Optional<Talk> talkOp = talksRepository.findById(id);
        return talkOp.map(TalkDto::fromTalk);
    }
}
