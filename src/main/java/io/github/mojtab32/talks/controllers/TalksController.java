package io.github.mojtab32.talks.controllers;

import io.github.mojtab32.talks.domains.Talk;
import io.github.mojtab32.talks.services.TalksService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TalksController {


    private final TalksService talksService;

    public TalksController(TalksService talksService) {
        this.talksService = talksService;
    }

    @GetMapping("/talks")
    public ResponseEntity<List<Talk>> getAllTalks() {
        return ResponseEntity.ok(talksService.getAllTalks());
    }

}
