package io.github.mojtab23.talks.controllers;

import io.github.mojtab23.talks.domains.Talk;
import io.github.mojtab23.talks.services.TalksService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

@RestController
public class TalksController {


    private final TalksService talksService;

    public TalksController(TalksService talksService) {
        this.talksService = talksService;
    }

    @GetMapping("/talks")
    public ResponseEntity<Page<Talk>> getAllTalks(@NotNull final Pageable pageable) {
        return ResponseEntity.ok(talksService.getAllTalks(pageable));
    }

}
