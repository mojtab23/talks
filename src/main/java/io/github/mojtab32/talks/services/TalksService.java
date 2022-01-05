package io.github.mojtab32.talks.services;

import io.github.mojtab32.talks.domains.Talk;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TalksService {

    List<Talk> getAllTalks();

}
