package com.felipegabrill.twitter.feed_service.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

@Schema(description = "Represents a single item in the feed")
public class FeedItemDTO {

    @Schema(
            description = "Identifier of the tweet",
            example = "tweet-123"
    )
    private String tweetId;

    @Schema(
            description = "Identifier of the tweet author",
            example = "user-456"
    )
    private String authorId;

    @Schema(
            description = "Identifier of the user who owns this feed",
            example = "user-789"
    )
    private String userId;

    @Schema(
            description = "Ranking score used to order feed items",
            example = "150"
    )
    private Long score;

    @Schema(
            description = "Creation timestamp of the feed item",
            example = "2026-02-05T12:30:00Z"
    )
    private Instant createdAt;

    public FeedItemDTO() {}

    public FeedItemDTO(String tweetId, String authorId, String userId, Long score, Instant createdAt) {
        this.tweetId = tweetId;
        this.authorId = authorId;
        this.userId = userId;
        this.score = score;
        this.createdAt = createdAt;
    }

    public String getTweetId() {
        return tweetId;
    }

    public void setTweetId(String tweetId) {
        this.tweetId = tweetId;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Long getScore() {
        return score;
    }

    public void setScore(Long score) {
        this.score = score;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
