package com.felipegabrill.twitter.tweet_service.dtos.tweet.response;

import com.felipegabrill.twitter.tweet_service.database.model.enums.TweetType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.UUID;

@Schema(description = "Response DTO for a retweet")
public class RetweetResponseDTO extends BaseTweetResponseDTO {

    @Schema(
            description = "Type of the tweet",
            example = "RETWEET"
    )
    private final TweetType type = TweetType.RETWEET;

    @Schema(
            description = "ID of the original tweet that was retweeted",
            example = "333e8400-e29b-41d4-a716-446655440000"
    )
    private final UUID retweetOfId;

    public RetweetResponseDTO(
            UUID id,
            UUID authorId,
            Integer likeCount,
            Instant createdAt,
            UUID retweetOfId
    ) {
        super(id, authorId, likeCount, createdAt);
        this.retweetOfId = retweetOfId;
    }

    public TweetType getType() {
        return type;
    }

    public UUID getRetweetOfId() {
        return retweetOfId;
    }
}
