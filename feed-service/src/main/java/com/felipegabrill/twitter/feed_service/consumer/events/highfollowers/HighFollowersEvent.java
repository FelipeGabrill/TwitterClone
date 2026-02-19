package com.felipegabrill.twitter.feed_service.consumer.events.highfollowers;

public record HighFollowersEvent(
        String eventId,
        String eventType,
        HighFollowersEventData data
) {
}

