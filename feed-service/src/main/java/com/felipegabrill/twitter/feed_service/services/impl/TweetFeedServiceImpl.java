package com.felipegabrill.twitter.feed_service.services.impl;

import com.felipegabrill.twitter.feed_service.database.rds.entities.TweetEntity;
import com.felipegabrill.twitter.feed_service.database.rds.repositories.TweetRepository;
import com.felipegabrill.twitter.feed_service.database.rds.repositories.result.PageResultTweet;
import com.felipegabrill.twitter.feed_service.infrastructure.util.TweetCursor;
import com.felipegabrill.twitter.feed_service.services.IFamousUserService;
import com.felipegabrill.twitter.feed_service.services.ITweetFeedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class TweetFeedServiceImpl implements ITweetFeedService {

    private static final Logger log =
            LoggerFactory.getLogger(TweetFeedServiceImpl.class);

    private final TweetRepository tweetRepository;
    private final IFamousUserService famousUserService;

    public TweetFeedServiceImpl(
            TweetRepository tweetRepository, IFamousUserService famousUserService
    ) {
        this.tweetRepository = tweetRepository;
        this.famousUserService = famousUserService;
    }

    @Override
    public void processTweetEvent(UUID tweetId,
                                  UUID authorId,
                                  Instant occurredAt) {

        if (tweetId == null || authorId == null) {
            throw new IllegalArgumentException(
                    "tweetData, tweetId or authorId is missing"
            );
        }

        boolean isFamous = famousUserService.isUserFamous(authorId);
        if (isFamous) {
            log.info("Tweet by famous user {} ignored for fan-out | tweetId={}",
                    authorId, tweetId);
            TweetEntity tweet = new TweetEntity();
            tweet.setId(UUID.randomUUID());
            tweet.setTweetId(tweetId);
            tweet.setAuthorId(authorId);
            tweet.setFeedType("FAMOUS_FEED");
            tweet.setCreatedAt(occurredAt);
            tweetRepository.save(tweet);
        }
    }

    @Override
    public PageResultTweet<TweetEntity> getRecentFamousTweets(
            UUID userId,
            int limit,
            TweetCursor lastCursor
    ) {
        if (userId == null) throw new IllegalArgumentException("userId cannot be null");

        int safeLimit = (limit <= 0 || limit > 100) ? 20 : limit;

        Instant lastCreatedAt = null;
        UUID lastTweetId = null;

        if (lastCursor != null) {
            lastCreatedAt = lastCursor.createdAt();
            lastTweetId = lastCursor.tweetId();
        }

        List<TweetEntity> items = tweetRepository.findRecentFamousTweets(
                userId, lastCreatedAt, lastTweetId, Pageable.ofSize(safeLimit)
        );

        TweetCursor nextCursor = null;
        if (!items.isEmpty()) {
            TweetEntity lastTweet = items.get(items.size() - 1);
            nextCursor = new TweetCursor(lastTweet.getCreatedAt(), lastTweet.getTweetId());
        }

        return new PageResultTweet<>(items, nextCursor);
    }

}
