package io.github.mojtab23.talks.dtos;

import io.github.mojtab23.talks.domains.Talk;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Duration;
import java.time.Instant;


public class RegisterTalkDto {

    @NotBlank
    private String title;
    private String description;
    @NotBlank
    private String speakerId;
    @NotNull
    private Instant planedStartTime;
    @NotNull
    private Duration planedDuration;

    public RegisterTalkDto() {
    }

    public RegisterTalkDto(String title, String description, String speakerId, Instant planedStartTime,
                           Duration planedDuration) {
        this.title = title;
        this.description = description;
        this.speakerId = speakerId;
        this.planedStartTime = planedStartTime;
        this.planedDuration = planedDuration;
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

    public Instant getPlanedStartTime() {
        return planedStartTime;
    }

    public void setPlanedStartTime(Instant planedStartTime) {
        this.planedStartTime = planedStartTime;
    }

    public Duration getPlanedDuration() {
        return planedDuration;
    }

    public void setPlanedDuration(Duration planedDuration) {
        this.planedDuration = planedDuration;
    }

    public Talk toTalk() {

        final Instant now = Instant.now();

        final Instant planedEndTime = this.planedStartTime.plus(planedDuration);

        return new Talk(
                null,
                this.title,
                this.description,
                this.speakerId,
                now,
                now,
                null,
                this.planedStartTime,
                planedEndTime,
                null,
                null
        );
    }

}
