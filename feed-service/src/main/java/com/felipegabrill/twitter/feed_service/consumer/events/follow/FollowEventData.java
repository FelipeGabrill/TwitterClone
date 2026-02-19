package com.felipegabrill.twitter.feed_service.consumer.events.follow;

import java.util.UUID;

public record FollowEventData(
        UUID followerId,
        UUID followedId
) {}
