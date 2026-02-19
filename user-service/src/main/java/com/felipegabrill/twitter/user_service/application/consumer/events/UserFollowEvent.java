package com.felipegabrill.twitter.user_service.application.consumer.events;

public record UserFollowEvent(
        String eventId,
        String eventType,
        UserFollowEventData data
) {
}
