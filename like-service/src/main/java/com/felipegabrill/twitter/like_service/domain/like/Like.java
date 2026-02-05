package com.felipegabrill.twitter.like_service.domain.like;

import java.time.Instant;
import java.util.UUID;

public class Like {

    private UUID id;

    private UUID tweetId;

    private UUID userId;

    private Instant createdAt;

    public Like() {
    }

    public Like(UUID id, UUID tweetId, Instant createdAt, UUID userId) {
        this.id = id;
        this.tweetId = tweetId;
        this.createdAt = createdAt;
        this.userId = userId;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getTweetId() {
        return tweetId;
    }

    public void setTweetId(UUID tweetId) {
        this.tweetId = tweetId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}