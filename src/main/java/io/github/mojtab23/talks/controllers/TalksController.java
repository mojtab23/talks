package io.github.mojtab23.talks.controllers;

import io.github.mojtab23.talks.dtos.RegisterTalkDto;
import io.github.mojtab23.talks.dtos.TalkDto;
import io.github.mojtab23.talks.services.TalksService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.Optional;

@RestController
public class TalksController {


    private final TalksService talksService;

    public TalksController(TalksService talksService) {
        this.talksService = talksService;
    }

    @GetMapping("/talks")
    public ResponseEntity<Page<TalkDto>> getAllTalks(@NotNull final Pageable pageable) {
        return ResponseEntity.ok(talksService.getAllTalks(pageable));
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

}
