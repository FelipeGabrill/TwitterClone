package com.felipegabrill.twitter.feed_service.consumer.events.tweet;

public record TweetEvent(
        String eventId,
        String eventType,
        TweetEventData data
) {
}