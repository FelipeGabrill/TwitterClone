package com.felipegabrill.twitter.follow_service.dtos.follow;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.UUID;

@Schema(description = "Data transfer object representing a follow relationship between users")
public class FollowResponseDTO {

    @Schema(description = "Unique identifier of the follow record", example = "d290f1ee-6c54-4b01-90e6-d701748f0851")
    private UUID id;

    @Schema(description = "UUID of the follower user", example = "1a2b3c4d-5e6f-7a8b-9c0d-1e2f3a4b5c6d")
    private UUID followerId;

    @Schema(description = "UUID of the user being followed", example = "6d5c4b3a-2f1e-0d9c-8b7a-6f5e4d3c2b1a")
    private UUID followingId;

    @Schema(description = "Timestamp when the follow relationship was created", example = "2026-02-03T12:34:56.789Z")
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

    public UUID getFollowerId() {
        return followerId;
    }

    public void setFollowerId(UUID followerId) {
        this.followerId = followerId;
    }

    public UUID getFollowingId() {
        return followingId;
    }

    public void setFollowingId(UUID followingId) {
        this.followingId = followingId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
