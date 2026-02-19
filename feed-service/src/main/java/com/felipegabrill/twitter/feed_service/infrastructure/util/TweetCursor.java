package com.felipegabrill.twitter.feed_service.infrastructure.util;

import java.time.Instant;
import java.util.UUID;

public record TweetCursor(Instant createdAt, UUID tweetId) {}

