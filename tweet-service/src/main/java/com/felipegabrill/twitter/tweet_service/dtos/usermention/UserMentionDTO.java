package com.felipegabrill.twitter.tweet_service.dtos.usermention;

import java.util.UUID;

public class UserMentionDTO {
    private String screenName;
    private UUID userId;
    private Integer startIndex;
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
