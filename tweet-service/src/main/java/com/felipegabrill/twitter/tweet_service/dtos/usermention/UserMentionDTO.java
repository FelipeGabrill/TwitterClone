package com.felipegabrill.twitter.tweet_service.dtos.usermention;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.UUID;

public class UserMentionDTO {

    @NotBlank(message = "Screen name cannot be blank")
    private String screenName;

    @NotNull(message = "User id is required")
    private UUID userId;

    @NotNull(message = "Start index is required")
    @PositiveOrZero(message = "Start index must be zero or positive")
    private Integer startIndex;

    @NotNull(message = "End index is required")
    @PositiveOrZero(message = "End index must be zero or positive")
    private Integer endIndex;

    public UserMentionDTO() {}

    public UserMentionDTO(String screenName, UUID userId, Integer startIndex, Integer endIndex) {
        this.screenName = screenName;
        this.userId = userId;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public Integer getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(Integer startIndex) {
        this.startIndex = startIndex;
    }

    public Integer getEndIndex() {
        return endIndex;
    }

    public void setEndIndex(Integer endIndex) {
        this.endIndex = endIndex;
    }
}
