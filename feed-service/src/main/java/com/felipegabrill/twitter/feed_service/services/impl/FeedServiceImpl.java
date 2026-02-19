package com.felipegabrill.twitter.feed_service.services.impl;

import com.felipegabrill.twitter.feed_service.database.dynamodb.entities.Feed;
import com.felipegabrill.twitter.feed_service.database.dynamodb.repositories.IFeedRepository;
import com.felipegabrill.twitter.feed_service.database.dynamodb.repositories.result.PageResult;
import com.felipegabrill.twitter.feed_service.database.rds.entities.TweetEntity;
import com.felipegabrill.twitter.feed_service.dtos.FeedItemDTO;
import com.felipegabrill.twitter.feed_service.dtos.response.FeedResponseDTO;
import com.felipegabrill.twitter.feed_service.infrastructure.mappers.FeedMapper;
import com.felipegabrill.twitter.feed_service.infrastructure.util.CursorUtil;
import com.felipegabrill.twitter.feed_service.database.rds.repositories.result.PageResultTweet;
import com.felipegabrill.twitter.feed_service.infrastructure.util.TweetCursor;
import com.felipegabrill.twitter.feed_service.services.IFeedService;
import com.felipegabrill.twitter.feed_service.services.IFollowService;
import com.felipegabrill.twitter.feed_service.services.IFamousUserService;
import com.felipegabrill.twitter.feed_service.services.ITweetFeedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static com.felipegabrill.twitter.feed_service.infrastructure.util.CursorUtil.decodeRds;

@Service
public class FeedServiceImpl implements IFeedService {

    private static final Logger log = LoggerFactory.getLogger(FeedServiceImpl.class);

    @Value("${feed.max-limit:100}")
    private int maxLimit;

    private final IFeedRepository feedRepository;
    private final FeedMapper feedMapper;
    private final IFollowService followService;
    private final IFamousUserService famousUserService;
    private final ITweetFeedService tweetFeedService;

    public FeedServiceImpl(
            IFeedRepository feedRepository,
            FeedMapper feedMapper,
            IFollowService followService,
            IFamousUserService famousUserService,
            ITweetFeedService tweetFeedService
    ) {
        this.feedRepository = feedRepository;
        this.feedMapper = feedMapper;
        this.followService = followService;
        this.famousUserService = famousUserService;
        this.tweetFeedService = tweetFeedService;
    }

    @Override
    public FeedResponseDTO getUserFeed(String userId, Integer limit, String lastRdsKeyStr, String lastDynamoKeyStr) {
        validateUserId(userId);
        int safeLimit = normalizeLimit(limit);
        log.info("Fetching balanced feed | userId={} | limit={}", userId, safeLimit);

        UUID userUuid = UUID.fromString(userId);
        TweetCursor lastRdsCursor = decodeRds(lastRdsKeyStr);
        Map<String, AttributeValue> lastEvaluatedKey = decodeCursor(lastDynamoKeyStr);

        List<String> followedUserIds = getFollowedUserIds(userUuid);
        List<Feed> famousFeed = fetchFamousFeed(userUuid, safeLimit, lastRdsCursor, followedUserIds);
        PageResultTweet<Feed> famousFeedPage = buildFamousPage(famousFeed, userUuid, safeLimit, lastRdsCursor, followedUserIds);

        PageResult<Feed> normalFeed = fetchNormalFeed(userId, safeLimit, lastEvaluatedKey);

        List<Feed> merged = buildBalancedFeed(famousFeed, normalFeed.items(), safeLimit);

        String rdsCursor = famousFeedPage != null ? CursorUtil.encode(famousFeedPage.lastKey()) : null;
        String dynamoCursor = normalFeed.lastKey() != null ? CursorUtil.encode(normalFeed.lastKey()) : null;

        log.info("RDS cursor={}", rdsCursor);
        log.info("DynamoDB cursor={}", dynamoCursor);

        return buildFeedResponse(merged, userId, rdsCursor, dynamoCursor);
    }

    private List<String> getFollowedUserIds(UUID userUuid) {
        List<String> followed = followService.getFollowedUsers(userUuid);
        log.info("User {} follows {} users", userUuid, followed.size());
        return followed;
    }

    private List<Feed> fetchFamousFeed(UUID userUuid, int limit, TweetCursor lastRdsCursor, List<String> followedUserIds) {
        if (followedUserIds.isEmpty()) return Collections.emptyList();

        PageResultTweet<TweetEntity> famousPage = tweetFeedService.getRecentFamousTweets(userUuid, limit, lastRdsCursor);
        List<Feed> famousFeed = famousPage.items().stream()
                .map(tweet -> feedMapper.toUserFeed(tweet.getTweetId(), tweet.getAuthorId(), userUuid.toString(), tweet.getCreatedAt()))
                .collect(Collectors.toList());
        log.info("Fetched famous tweets | count={}", famousFeed.size());
        return famousFeed;
    }

    private PageResultTweet<Feed> buildFamousPage(List<Feed> famousFeed, UUID userUuid, int limit, TweetCursor lastRdsCursor, List<String> followedUserIds) {
        if (famousFeed.isEmpty()) return null;
        PageResultTweet<TweetEntity> famousPage = tweetFeedService.getRecentFamousTweets(userUuid, limit, lastRdsCursor);
        return new PageResultTweet<>(famousFeed, famousPage.lastKey());
    }

    private PageResult<Feed> fetchNormalFeed(String userId, int limit, Map<String, AttributeValue> lastEvaluatedKey) {
        PageResult<Feed> normalFeed = feedRepository.getUserFeedByDate(userId, limit, lastEvaluatedKey);
        log.info("Normal feed retrieved | items={}", normalFeed.items().size());
        return normalFeed;
    }

    private List<Feed> buildBalancedFeed(List<Feed> famousFeed, List<Feed> normalFeed, int limit) {
        int famousCount = famousFeed.size();
        double ratio = calculateFamousRatio(famousCount);
        int maxFamousItems = (int) Math.round(limit * ratio);

        List<Feed> balancedFamous = famousFeed.stream().limit(maxFamousItems).collect(Collectors.toList());
        List<Feed> balancedNormal = normalFeed.stream().limit(limit - balancedFamous.size()).collect(Collectors.toList());

        log.info("Balanced feed | famousItems={}, normalItems={}", balancedFamous.size(), balancedNormal.size());
        return mergeAndSortFeeds(balancedNormal, balancedFamous, limit);
    }


    @Override
    public FeedResponseDTO getGlobalFeed(Integer limit, String lastKey) {
        int safeLimit = normalizeLimit(limit);
        log.info("Fetching global feed. limit={}, hasCursor={}", safeLimit, lastKey != null);

        Map<String, AttributeValue> lastEvaluatedKey = decodeCursor(lastKey);
        PageResult<Feed> globalFeed = feedRepository.getGlobalFeedByDate(safeLimit, lastEvaluatedKey);

        String dynamoCursor = globalFeed.lastKey() != null ? CursorUtil.encode(globalFeed.lastKey()) : null;

        return buildFeedResponse(globalFeed.items(), null, null, dynamoCursor);
    }

    @Override
    public void handleNewTweet(UUID tweetId,
                               String tweetType,
                               UUID authorId,
                               UUID rootTweetId,
                               Instant occurredAt) {

        Instant createdAt = occurredAt != null ? occurredAt : Instant.now();
        log.info("Processing new tweet | tweetId={} | authorId={} | tweetType={}", tweetId, authorId, tweetType);

        Feed globalFeed = feedMapper.toGlobalFeed(tweetId, authorId, createdAt);
        feedRepository.saveGlobalFeedItem(globalFeed);

        if (famousUserService.isUserFamous(authorId)) {
            log.info("Author {} is famous, skipping fan-out for tweetId={}", authorId, tweetId);
            tweetFeedService.processTweetEvent(tweetId, authorId, createdAt);
            return;
        }

        List<String> followers = followService.getFollowedUsers(authorId);
        if (followers == null || followers.isEmpty()) return;

        followers.stream()
                .filter(Objects::nonNull)
                .map(follower -> feedMapper.toUserFeed(tweetId, authorId, follower, createdAt))
                .forEach(feedRepository::saveUserFeedItem);

        log.info("Fan-out completed | tweetId={} | totalFollowers={}", tweetId, followers.size());
    }

    private List<Feed> mergeAndSortFeeds(List<Feed> normalFeed, List<Feed> famousFeed, int limit) {
        List<Feed> combined = new ArrayList<>(normalFeed.size() + famousFeed.size());
        combined.addAll(normalFeed);
        combined.addAll(famousFeed);

        combined.sort(Comparator.comparing(Feed::getCreatedAt).reversed());
        return combined.size() > limit ? combined.subList(0, limit) : combined;
    }

    private Map<String, AttributeValue> decodeCursor(String cursor) {
        return cursor != null ? CursorUtil.decode(cursor) : null;
    }

    private FeedResponseDTO buildFeedResponse(List<Feed> feeds, String userId,
                                              String rdsCursor, String dynamoCursor) {
        List<FeedItemDTO> itemsDto = feeds.stream()
                .map(feed -> feedMapper.toDto(feed, userId))
                .toList();

        return new FeedResponseDTO(itemsDto, rdsCursor, dynamoCursor);
    }

    private void validateUserId(String userId) {
        if (userId == null || userId.isBlank()) {
            throw new IllegalArgumentException("userId cannot be null or blank");
        }
    }

    private int normalizeLimit(Integer limit) {
        if (limit > maxLimit) {
            log.debug("Limit {} exceeds MAX_LIMIT {}, using MAX_LIMIT", limit, maxLimit);
            return maxLimit;
        }

        return limit;
    }

    private double calculateFamousRatio(int famousCount) {
        if (famousCount == 0) return 0.0;
        if (famousCount < 10) return 0.2;
        if (famousCount < 50) return 0.35;
        return 0.5;
    }
}
