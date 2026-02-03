package com.felipegabrill.twitter.tweet_service.dtos.tweet.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.UUID;

@Schema(
        description = "Base response for all tweet types",
        discriminatorProperty = "type",
        oneOf = {
                NormalTweetResponseDTO.class,
                ReplyTweetResponseDTO.class,
                RetweetResponseDTO.class,
                QuoteTweetResponseDTO.class
        }
)
public abstract class BaseTweetResponseDTO {

    @Schema(
            description = "Unique identifier of the tweet",
            example = "123e4567-e89b-12d3-a456-426614174000"
    )
    private final UUID id;

    @Schema(
            description = "ID of the user who created the tweet",
            example = "987e6543-e21b-12d3-a456-426614174999"
    )
    private final UUID authorId;

    @Schema(
            description = "Total number of likes for this tweet",
            example = "42"
    )
    private final Integer likeCount;

    @Schema(
            description = "Timestamp when the tweet was created (UTC)",
            example = "2026-02-01T10:15:30Z"
    )
    private final Instant createdAt;

    protected BaseTweetResponseDTO(
            UUID id,
            UUID authorId,
            Integer likeCount,
            Instant createdAt
    ) {
        this.id = id;
        this.authorId = authorId;
        this.likeCount = likeCount;
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public UUID getAuthorId() {
        return authorId;
    }

    public Integer getLikeCount() {
        return likeCount;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
