package com.felipegabrill.twitter.follow_service.dtos.follow;

import java.time.Instant;
import java.util.UUID;

public class FollowResponseDTO {

    private UUID id;
    private UUID followerId;
    private UUID followingId;
    private Instant createdAt;

    public FollowResponseDTO() {
    }

    public FollowResponseDTO(UUID id, UUID followerId, UUID followingId, Instant createdAt) {
        this.id = id;
        this.followerId = followerId;
        this.followingId = followingId;
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public UUID getFollowingId() {
        return followingId;
    }

    public void setFollowingId(UUID followingId) {
        this.followingId = followingId;
    }

    public UUID getFollowerId() {
        return followerId;
    }

    public void setFollowerId(UUID followerId) {
        this.followerId = followerId;
    }
}
