package com.felipegabrill.twitter.feed_service.dtos;

import java.time.Instant;

public class FeedItemDTO {

    private String tweetId;
    private String authorId;
    private String userId;
    private Long score;
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
