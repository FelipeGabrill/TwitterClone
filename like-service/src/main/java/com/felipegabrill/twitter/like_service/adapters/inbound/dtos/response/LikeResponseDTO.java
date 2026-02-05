package com.felipegabrill.twitter.like_service.adapters.inbound.dtos.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.UUID;

@Schema(description = "Like response representation")
public class LikeResponseDTO {

    @Schema(
            description = "ID of the user who liked the tweet",
            example = "123e4567-e89b-12d3-a456-426614174000"
    )
    private final UUID userId;

    @Schema(
            description = "Timestamp when the like was created",
            example = "2026-02-05T02:30:00Z"
    )
    private final Instant createdAt;

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