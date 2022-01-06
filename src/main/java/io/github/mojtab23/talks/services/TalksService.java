package io.github.mojtab23.talks.services;

import io.github.mojtab23.talks.domains.Talk;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TalksService {

    Page<Talk> getAllTalks(Pageable pageable);

}
