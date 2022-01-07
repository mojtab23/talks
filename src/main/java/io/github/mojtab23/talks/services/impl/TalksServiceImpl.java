package io.github.mojtab23.talks.services.impl;

import io.github.mojtab23.talks.domains.Talk;
import io.github.mojtab23.talks.domains.UserRole;
import io.github.mojtab23.talks.dtos.RegisterTalkDto;
import io.github.mojtab23.talks.dtos.TalkDto;
import io.github.mojtab23.talks.dtos.UserDto;
import io.github.mojtab23.talks.repositories.TalksRepository;
import io.github.mojtab23.talks.services.TalksService;
import io.github.mojtab23.talks.services.UsersService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

@Service
public class TalksServiceImpl implements TalksService {

    private final TalksRepository talksRepository;
    private final UsersService usersService;

    public TalksServiceImpl(TalksRepository talksRepository, UsersService usersService) {
        this.talksRepository = talksRepository;
        this.usersService = usersService;
    }

    @Override
    public Page<TalkDto> getAllTalks(Instant from, Instant to, Pageable pageable) {
        return talksRepository.findTalksBetween(from, to, pageable).map(TalkDto::fromTalk);
    }

    @Override
    public Page<TalkDto> getTalksBySpeakerId(String speakerId, Pageable pageable) {
        return talksRepository.findAllBySpeakerId(speakerId, pageable).map(TalkDto::fromTalk);
    }

    @Transactional
    @Override
    public String registerTalk(RegisterTalkDto dto) {
        final Talk talk = dto.toTalk();

        if (!userHasRoles(talk.getSpeakerId())) {
            return null;
        }

        final Integer overlapingTalks = talksRepository.countSpeakersOverlapingTalks(talk.getSpeakerId(), talk.getPlanedStartTime(), talk.getPlanedEndTime());
        if (overlapingTalks > 0) {
            return null;
        }

        final Talk savedTalk = talksRepository.save(talk);
        return savedTalk.getId();
    }

    private boolean userHasRoles(String speakerId) {
        final Optional<UserDto> user = usersService.getUser(speakerId);
        return user
                .map(userDto -> userDto.getRoles().contains(UserRole.ADMIN) || userDto.getRoles().contains(UserRole.SPEAKER))
                .orElse(false);
    }

    @Override
    public Optional<TalkDto> getTalkById(String id) {
        final Optional<Talk> talkOp = talksRepository.findById(id);
        return talkOp.map(TalkDto::fromTalk);
    }
}
