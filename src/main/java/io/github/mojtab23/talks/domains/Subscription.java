package io.github.mojtab23.talks.domains;

import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document
@CompoundIndex(name = "attendee_talk_unique", def = "{'attendeeId': 1, 'talkId': 1}", unique = true)
public class Subscription {
    private String id;
    private String attendeeId;
    private String talkId;
    private Instant createdAt;
    private Instant deletedAt;

    public Subscription() {
    }

    public Subscription(String id, String attendeeId, String talkId, Instant createdAt, Instant deletedAt) {
        this.id = id;
        this.attendeeId = attendeeId;
        this.talkId = talkId;
        this.createdAt = createdAt;
        this.deletedAt = deletedAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAttendeeId() {
        return attendeeId;
    }

    public void setAttendeeId(String attendeeId) {
        this.attendeeId = attendeeId;
    }

    public String getTalkId() {
        return talkId;
    }

    public void setTalkId(String talkId) {
        this.talkId = talkId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Instant deletedAt) {
        this.deletedAt = deletedAt;
    }
}
