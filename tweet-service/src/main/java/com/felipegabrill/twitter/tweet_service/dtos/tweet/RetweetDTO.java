package com.felipegabrill.twitter.tweet_service.dtos.tweet;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class RetweetDTO {

    @NotNull(message = "Tweet id is required")
    private UUID tweetId;

    public RetweetDTO() {
    }

    public RetweetDTO(UUID tweetId) {
        this.tweetId = tweetId;
    }

    public UUID getTweetId() {
        return tweetId;
    }

    public void setTweetId(UUID tweetId) {
        this.tweetId = tweetId;
    }
}
