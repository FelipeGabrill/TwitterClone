package com.felipegabrill.twitter.like_service.adapters.inbound.dtos.response;

import java.time.Instant;
import java.util.UUID;

public class LikeResponseDTO {

    private UUID userId;
    private Instant createdAt;

    public LikeResponseDTO() {
    }

    public LikeResponseDTO(UUID userId, Instant createdAt) {
        this.userId = userId;
        this.createdAt = createdAt;
    }

    public UUID getUserId() {
        return userId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}