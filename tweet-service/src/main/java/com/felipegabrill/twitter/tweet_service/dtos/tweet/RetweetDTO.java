package com.felipegabrill.twitter.tweet_service.dtos.tweet;

import java.util.UUID;

public class RetweetDTO {

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
