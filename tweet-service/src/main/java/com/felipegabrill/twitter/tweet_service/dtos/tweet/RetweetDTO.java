package com.felipegabrill.twitter.tweet_service.dtos.tweet;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@Schema(
        name = "RetweetDTO",
        description = "DTO used to retweet an existing tweet. Only the target tweet ID is required."
)
public class RetweetDTO {

    @Schema(
            description = "ID of the tweet to be retweeted",
            example = "550e8400-e29b-41d4-a716-446655440000",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Tweet id is required")
    private UUID tweetId;

    public RetweetDTO() {
    }

    public RetweetDTO(UUID tweetId) {
        this.tweetId = tweetId;
    }

    public UUID getTweetId() {
        return tweetId;
    }

    public void setTweetId(UUID tweetId) {
        this.tweetId = tweetId;
    }
}
