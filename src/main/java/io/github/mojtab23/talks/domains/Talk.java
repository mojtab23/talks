package io.github.mojtab23.talks.domains;


import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document
public class Talk {

    private String id;
    private String title;
    private String description;
    // owner (User)
    private String speakerId;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;
    private Instant planedStartTime;
    private Instant planedEndTime;
    private Instant startedAt;
    private Instant endedAt;

    public Talk() {
    }

    public Talk(String title, String description, String speakerId, Instant planedStartTime, Instant planedEndTime) {
        this.title = title;
        this.description = description;
        this.speakerId = speakerId;
        final Instant now = Instant.now();
        this.createdAt = now;
        this.updatedAt = now;
        this.deletedAt = null;
        this.planedStartTime = planedStartTime;
        this.planedEndTime = planedEndTime;
        this.startedAt = null;
        this.endedAt = null;
    }

    public Talk(
            String id, String title, String description, String speakerId, Instant createdAt, Instant updatedAt,
            Instant deletedAt, Instant planedStartTime, Instant planedEndTime, Instant startedAt, Instant endedAt
    ) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.speakerId = speakerId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
        this.planedStartTime = planedStartTime;
        this.planedEndTime = planedEndTime;
        this.startedAt = startedAt;
        this.endedAt = endedAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSpeakerId() {
        return speakerId;
    }

    public void setSpeakerId(String speakerId) {
        this.speakerId = speakerId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Instant deletedAt) {
        this.deletedAt = deletedAt;
    }

    public Instant getPlanedStartTime() {
        return planedStartTime;
    }

    public void setPlanedStartTime(Instant planedStartTime) {
        this.planedStartTime = planedStartTime;
    }

    public Instant getPlanedEndTime() {
        return planedEndTime;
    }

    public void setPlanedEndTime(Instant planedEndTime) {
        this.planedEndTime = planedEndTime;
    }

    public Instant getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(Instant startedAt) {
        this.startedAt = startedAt;
    }

    public Instant getEndedAt() {
        return endedAt;
    }

    public void setEndedAt(Instant endedAt) {
        this.endedAt = endedAt;
    }

    @Override
    public String toString() {
        return "Talk{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", speakerId='" + speakerId + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", deletedAt=" + deletedAt +
                ", planedStartTime=" + planedStartTime +
                ", planedEndTime=" + planedEndTime +
                ", startedAt=" + startedAt +
                ", endedAt=" + endedAt +
                '}';
    }
}
