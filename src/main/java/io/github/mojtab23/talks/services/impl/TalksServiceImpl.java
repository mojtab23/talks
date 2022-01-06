package io.github.mojtab23.talks.services.impl;

import io.github.mojtab23.talks.domains.Talk;
import io.github.mojtab23.talks.services.TalksService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TalksServiceImpl implements TalksService {
    @Override
    public List<Talk> getAllTalks() {
        return null;
    }
}
