package io.github.mojtab23.talks.controllers;

import io.github.mojtab23.talks.dtos.RegisterTalkDto;
import io.github.mojtab23.talks.dtos.TalkDto;
import io.github.mojtab23.talks.services.TalksService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.time.Instant;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
public class TalksController {


    private final TalksService talksService;

    public TalksController(TalksService talksService) {
        this.talksService = talksService;
    }

    @GetMapping("/talks")
    public ResponseEntity<Page<TalkDto>> getAllTalks(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date to,
            @NotNull final Pageable pageable
    ) {
        final Instant f = from != null ? from.toInstant() : Instant.MIN;
        final Instant t = to != null ? to.toInstant() : Instant.MAX;
        return ResponseEntity.ok(talksService.getAllTalks(f, t, pageable));
    }

    @GetMapping("/talks/search")
    public ResponseEntity<List<TalkDto>> searchTalks(
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "speakerId", required = false) String speakerId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date planedStartFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date planedStartTo,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date planedEndFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date planedEndTo,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date startedFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date startedTo,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date endedFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date endedTo
    ) {
        return ResponseEntity.ok(talksService.searchTalks(
                title,
                description,
                speakerId,
                planedStartFrom,
                planedStartTo,
                planedEndFrom,
                planedEndTo,
                startedFrom,
                startedTo,
                endedFrom,
                endedTo
        ));
    }

    @GetMapping("/talks/{id}")
    public ResponseEntity<TalkDto> getTalkById(@PathVariable String id) {
        final Optional<TalkDto> talk = talksService.getTalkById(id);
        if (talk.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(talk.get());
    }

    @GetMapping("/talks/speaker/{id}")
    public ResponseEntity<Page<TalkDto>> getTalksBySpeaker(@PathVariable("id") String speakerId, @NotNull final Pageable pageable) {
        return ResponseEntity.ok(talksService.getTalksBySpeakerId(speakerId, pageable));
    }

    @PostMapping("/talks")
    public ResponseEntity<String> registerTalk(@RequestBody @Valid RegisterTalkDto dto) {
        String talkId = talksService.registerTalk(dto);
        if (talkId == null) {
            return ResponseEntity.badRequest().body("can't insert the talk");
        } else return ResponseEntity.created(URI.create("/talks/" + talkId)).build();

    }

    @PostMapping("/talks/{id}/subscribers")
    public ResponseEntity subscribeTalk(@PathVariable("id") String talkId, @RequestBody @NotBlank String attendeeId) {
        final String subscriptionId = talksService.subscribeToTalk(talkId, attendeeId);
        if (subscriptionId == null) {
            return ResponseEntity.badRequest().body("can't subscribe to the talk");
        } else return ResponseEntity.ok(Collections.singletonMap("subscriptionId", subscriptionId));
    }

}
