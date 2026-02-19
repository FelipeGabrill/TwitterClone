package com.felipegabrill.twitter.feed_service.consumer.events.highfollowers;

import java.util.UUID;

public record HighFollowersEventData(
        UUID userId,
        long followersCount
) {
}
