package com.felipegabrill.twitter.tweet_service.consumer.events;

import java.util.UUID;

public record LikeEventData(
        UUID tweetId
) {
}
