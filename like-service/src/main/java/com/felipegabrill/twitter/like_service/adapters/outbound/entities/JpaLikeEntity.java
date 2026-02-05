package com.felipegabrill.twitter.like_service.adapters.outbound.entities;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "tweet_likes",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_tweet_user_like",
                columnNames = {"tweet_id", "user_id"}
        ),
        indexes = {
                @Index(name = "idx_tweet_like_tweet_id", columnList = "tweet_id"),
                @Index(name = "idx_tweet_like_user_id", columnList = "user_id")
        }
)
public class JpaLikeEntity {

    @Id
    private UUID id;

    private UUID tweetId;

    private UUID userId;

    private Instant createdAt;

    public JpaLikeEntity() {
    }

    public JpaLikeEntity(UUID tweetId, UUID userId) {
        this.tweetId = tweetId;
        this.userId = userId;
        this.createdAt = Instant.now();
    }

    public UUID getId() {
        return id;
    }

    public UUID getTweetId() {
        return tweetId;
    }

    public UUID getUserId() {
        return userId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setTweetId(UUID tweetId) {
        this.tweetId = tweetId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
