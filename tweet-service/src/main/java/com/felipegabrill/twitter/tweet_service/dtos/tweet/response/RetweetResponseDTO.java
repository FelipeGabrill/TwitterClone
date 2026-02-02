package com.felipegabrill.twitter.tweet_service.dtos.tweet.response;

import com.felipegabrill.twitter.tweet_service.database.model.enums.TweetType;

import java.time.Instant;
import java.util.UUID;

public class RetweetResponseDTO extends BaseTweetResponseDTO {

    private final TweetType type = TweetType.RETWEET;
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
