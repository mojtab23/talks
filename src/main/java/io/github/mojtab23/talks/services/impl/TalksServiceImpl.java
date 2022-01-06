package io.github.mojtab23.talks.services.impl;

import io.github.mojtab23.talks.dtos.TalkDto;
import io.github.mojtab23.talks.repositories.TalksRepository;
import io.github.mojtab23.talks.services.TalksService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
}
