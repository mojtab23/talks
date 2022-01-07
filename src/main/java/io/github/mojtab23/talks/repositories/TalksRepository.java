package io.github.mojtab23.talks.repositories;

import io.github.mojtab23.talks.domains.Talk;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.Instant;

public interface TalksRepository extends MongoRepository<Talk, String> {

    Page<Talk> findAllBySpeakerId(String speakerId, Pageable pageable);

    @Query("{\n" +
            "    $or: [\n" +
            "        {\n" +
            "            planedStartTime: {\n" +
            "                $gte: ?0,\n" +
            "                $lt: ?1 \n" +
            "            }\n" +
            "        },\n" +
            "        {\n" +
            "            planedEndTime: {\n" +
            "                $gte: ?0,\n" +
            "                $lt: ?1 \n" +
            "            }\n" +
            "\n" +
            "        }]\n" +
            "}")
    Page<Talk> findTalksBetween(Instant start, Instant end, Pageable pageable);

    @Query(value = "{\n" +
            "    $or: [\n" +
            "        {\n" +
            "            speakerId: ?0," +
            "            planedStartTime: {\n" +
            "                $gte: ?1,\n" +
            "                $lt: ?2 \n" +
            "            }\n" +
            "        },\n" +
            "        {\n" +
            "            speakerId: ?0," +
            "            planedEndTime: {\n" +
            "                $gte: ?1,\n" +
            "                $lt: ?2 \n" +
            "            }\n" +
            "\n" +
            "        }]\n" +
            "}", count = true)
    Integer countSpeakersOverlapingTalks(String speakerId, Instant start, Instant end);

}
