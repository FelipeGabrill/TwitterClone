package com.felipegabrill.twitter.tweet_service.consumer.events;

public record TweetLikeEvent(
        String eventId,
        String eventType,
        LikeEventData data
) {
}
