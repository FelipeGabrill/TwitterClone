package com.felipegabrill.twitter.tweet_service.dtos.tweet.response;

import java.time.Instant;
import java.util.UUID;

public abstract class BaseTweetResponseDTO {

    private final UUID id;
    private final UUID authorId;
    private final Integer likeCount;
    private final Instant createdAt;

    public BaseTweetResponseDTO(UUID id, UUID authorId, Integer likeCount, Instant createdAt) {
        this.id = id;
        this.authorId = authorId;
        this.likeCount = likeCount;
        this.createdAt = createdAt;
    }

    public Integer getLikeCount() {
        return likeCount;
    }

    public UUID getId() {
        return id;
    }
    public UUID getAuthorId() {
        return authorId;
    }
    public Instant getCreatedAt() {
        return createdAt;
    }
}