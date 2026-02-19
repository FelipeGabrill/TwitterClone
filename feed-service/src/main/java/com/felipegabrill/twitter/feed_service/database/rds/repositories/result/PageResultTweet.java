package com.felipegabrill.twitter.feed_service.database.rds.repositories.result;

import com.felipegabrill.twitter.feed_service.infrastructure.util.TweetCursor;

import java.util.List;

public record PageResultTweet<T>(List<T> items, TweetCursor lastKey) {}

