package com.felipegabrill.twitter.feed_service.database.dynamodb.repositories.impl;

import com.felipegabrill.twitter.feed_service.database.dynamodb.entities.Feed;
import com.felipegabrill.twitter.feed_service.database.dynamodb.repositories.IFeedRepository;
import com.felipegabrill.twitter.feed_service.database.dynamodb.repositories.result.PageResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.core.pagination.sync.SdkIterable;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.enhanced.dynamodb.model.*;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.Map;

@Repository
public class FeedRepositoryImpl implements IFeedRepository {

    private static final Logger log = LoggerFactory.getLogger(FeedRepositoryImpl.class);

    private final DynamoDbEnhancedClient enhancedClient;

    @Value("${feed.user.table}")
    private String userFeedTableName;

    @Value("${feed.global.table}")
    private String globalFeedTableName;

    public FeedRepositoryImpl(DynamoDbEnhancedClient enhancedClient) {
        this.enhancedClient = enhancedClient;
    }

    @Override
    public void saveUserFeedItem(Feed feedItem) {
        log.debug("Saving user feed item. pk={}, sk={}", feedItem.getPk(), feedItem.getSk());
        DynamoDbTable<Feed> table = enhancedClient.table(userFeedTableName, TableSchema.fromBean(Feed.class));
        table.putItem(feedItem);
        log.info("User feed item saved successfully");
    }

    @Override
    public void saveGlobalFeedItem(Feed feedItem) {
        log.debug("Saving global feed item. pk={}, sk={}", feedItem.getPk(), feedItem.getSk());
        DynamoDbTable<Feed> table = enhancedClient.table(globalFeedTableName, TableSchema.fromBean(Feed.class));
        table.putItem(feedItem);
        log.info("Global feed item saved successfully");
    }

    @Override
    public PageResult<Feed> getGlobalFeedByDate(int limit, Map<String, AttributeValue> lastEvaluatedKey) {
        log.info("Fetching global feed by date. limit={}", limit);
        DynamoDbTable<Feed> table = enhancedClient.table(globalFeedTableName, TableSchema.fromBean(Feed.class));
        DynamoDbIndex<Feed> gsi = table.index("GSI_DATE");

        QueryConditional query = QueryConditional.keyEqualTo(
                Key.builder().partitionValue("FEED#GLOBAL#FOR_YOU").build()
        );

        SdkIterable<Page<Feed>> pages = gsi.query(r -> r
                .queryConditional(query)
                .limit(limit)
                .exclusiveStartKey(lastEvaluatedKey)
                .scanIndexForward(false)
        );

        Page<Feed> firstPage = pages.iterator().next();
        log.info("Global feed page fetched. items={}, hasMore={}",
                firstPage.items().size(),
                firstPage.lastEvaluatedKey() != null
        );

        return new PageResult<>(firstPage.items(), firstPage.lastEvaluatedKey());
    }

    @Override
    public PageResult<Feed> getUserFeedByDate(String userId, int limit, Map<String, AttributeValue> lastEvaluatedKey) {
        log.info("Fetching user feed by date. userId={}, limit={}", userId, limit);

        String gsiPartitionKey = "USER#" + userId;
        DynamoDbTable<Feed> table = enhancedClient.table(userFeedTableName, TableSchema.fromBean(Feed.class));
        DynamoDbIndex<Feed> gsi = table.index("GSI_DATE");

        QueryConditional query = QueryConditional.keyEqualTo(
                Key.builder().partitionValue(gsiPartitionKey).build()
        );

        SdkIterable<Page<Feed>> pages = gsi.query(r -> r
                .queryConditional(query)
                .limit(limit)
                .exclusiveStartKey(lastEvaluatedKey)
                .scanIndexForward(false)
        );

        Page<Feed> firstPage = pages.iterator().next();
        log.info("User feed page fetched. items={}, hasMore={}",
                firstPage.items().size(),
                firstPage.lastEvaluatedKey() != null
        );

        firstPage.items().forEach(f ->
                log.info("FeedItem -> tweetId={}, authorId={}, createdAt={}", f.getTweetId(), f.getAuthorId(), f.getCreatedAt())
        );

        return new PageResult<>(firstPage.items(), firstPage.lastEvaluatedKey());
    }
}
