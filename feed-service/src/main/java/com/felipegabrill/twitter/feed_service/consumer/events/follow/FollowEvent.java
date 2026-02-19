package com.felipegabrill.twitter.feed_service.consumer.events.follow;

public record FollowEvent(
        String eventId,
        String eventType,
        FollowEventData data
) {}
