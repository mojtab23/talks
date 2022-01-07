package io.github.mojtab23.talks.services.impl;

import com.mongodb.MongoException;
import io.github.mojtab23.talks.domains.Subscription;
import io.github.mojtab23.talks.domains.Talk;
import io.github.mojtab23.talks.domains.UserRole;
import io.github.mojtab23.talks.dtos.RegisterTalkDto;
import io.github.mojtab23.talks.dtos.TalkDto;
import io.github.mojtab23.talks.dtos.UserDto;
import io.github.mojtab23.talks.repositories.SubscriptionsRepository;
import io.github.mojtab23.talks.repositories.TalksRepository;
import io.github.mojtab23.talks.services.TalksService;
import io.github.mojtab23.talks.services.UsersService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TalksServiceImpl implements TalksService {

    private final TalksRepository talksRepository;
    private final SubscriptionsRepository subscriptionsRepository;
    private final UsersService usersService;
    private final MongoTemplate mongoTemplate;

    public TalksServiceImpl(TalksRepository talksRepository, SubscriptionsRepository subscriptionsRepository, UsersService usersService, MongoTemplate mongoTemplate) {
        this.talksRepository = talksRepository;
        this.subscriptionsRepository = subscriptionsRepository;
        this.usersService = usersService;
        this.mongoTemplate = mongoTemplate;
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

        if (!userHasRolesForRegisteringTalk(talk.getSpeakerId())) {
            return null;
        }

        final Integer overlapingTalks = talksRepository.countSpeakersOverlapingTalks(talk.getSpeakerId(), talk.getPlanedStartTime(), talk.getPlanedEndTime());
        if (overlapingTalks > 0) {
            return null;
        }

        final Talk savedTalk = talksRepository.save(talk);
        return savedTalk.getId();
    }

    private boolean userHasRolesForRegisteringTalk(String speakerId) {
        final Optional<UserDto> user = usersService.getUser(speakerId);
        return user
                .map(userDto -> userDto.getRoles().contains(UserRole.ADMIN) || userDto.getRoles().contains(UserRole.SPEAKER))
                .orElse(false);
    }

    private boolean userHasRolesForAttendingTalk(String attendeeId) {
        final Optional<UserDto> user = usersService.getUser(attendeeId);
        return user
                .map(userDto -> userDto.getRoles().contains(UserRole.ATTENDEE))
                .orElse(false);
    }

    @Override
    public Optional<TalkDto> getTalkById(String id) {
        final Optional<Talk> talkOp = talksRepository.findById(id);
        return talkOp.map(TalkDto::fromTalk);
    }

    @Transactional
    @Override
    public String subscribeToTalk(String talkId, String attendeeId) {
        if (!userHasRolesForAttendingTalk(attendeeId)) {
            return null;
        }

        final Instant now = Instant.now();
        if (!checkIfTalkAcceptsSubscriptions(talkId, attendeeId, now)) {
            return null;
        }

        final Subscription subscription = new Subscription(null, attendeeId, talkId, now, null);
        final Subscription savedSubscription;
        try {
            savedSubscription = subscriptionsRepository.save(subscription);
        } catch (MongoException e) {
            return null;
        }
        return savedSubscription.getId();
    }

    @Override
    public List<TalkDto> searchTalks(
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
    ) {
        Criteria criteria = new Criteria();
        if (title != null) {
            criteria = criteria.and("title").regex(title);
        }
        if (description != null) {
            criteria = criteria.and("description").regex(description);
        }
        if (speakerId != null) {
            criteria = criteria.and("speakerId").is(speakerId);
        }
        // planedStart
        criteria = addDateRange("planedStartTime", planedStartFrom, planedStartTo, criteria);
        // planedEnd
        criteria = addDateRange("planedEndTime", planedEndFrom, planedEndTo, criteria);
        // started
        criteria = addDateRange("startedAt", startedFrom, startedTo, criteria);
        // ended
        criteria = addDateRange("endedAt", endedFrom, endedTo, criteria);


        return mongoTemplate.query(Talk.class).matching(criteria).stream().map(TalkDto::fromTalk).collect(Collectors.toList());
    }

    private Criteria addDateRange(String field, Date f, Date t, Criteria c) {
        if (!checkDateRange(f, t)) return c;
        return c.and(field).gte(f).lt(t);
    }

    private boolean checkDateRange(Date f, Date t) {
        return f != null && t != null && t.compareTo(f) > 0;
    }

    private boolean checkIfTalkAcceptsSubscriptions(String talkId, String attendeeId, Instant subscriptionTime) {
        final Optional<Talk> talk = talksRepository.findById(talkId);
        return talk
                .map(t -> t.getDeletedAt() == null &&
                        t.getPlanedEndTime().compareTo(subscriptionTime) > 0 &&
                        !t.getSpeakerId().equals(attendeeId))
                .orElse(false);
    }
}
