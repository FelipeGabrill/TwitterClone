package com.felipegabrill.twitter.user_service.application.consumer.events;

import java.time.Instant;
import java.util.UUID;

public record UserFollowEventData(
        UUID followerId,
        UUID followedId,
        Instant occurredAt
) {
}
