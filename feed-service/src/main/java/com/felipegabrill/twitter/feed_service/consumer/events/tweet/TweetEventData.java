package com.felipegabrill.twitter.feed_service.consumer.events.tweet;

import java.time.Instant;
import java.util.UUID;

public record TweetEventData(
        UUID tweetId,
        UUID authorId,
        String tweetType,
        UUID rootTweetId,
        Instant occurredAt
) {
}