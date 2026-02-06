package com.felipegabrill.twitter.feed_service.database.repositories.impl;

import com.felipegabrill.twitter.feed_service.database.entities.FeedEntity;
import com.felipegabrill.twitter.feed_service.database.repositories.IFeedRepository;
import com.felipegabrill.twitter.feed_service.database.repositories.result.PageResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.Map;

@Repository
public class FeedRepositoryImpl implements IFeedRepository {

    private final DynamoDbEnhancedClient enhancedClient;

    @Value("${feed.user.table}")
    private String userFeedTableName;

    @Value("${feed.global.table}")
    private String globalFeedTableName;

    public FeedRepositoryImpl(DynamoDbEnhancedClient enhancedClient) {
        this.enhancedClient = enhancedClient;
    }

    private DynamoDbTable<FeedEntity> userFeedTable() {
        return enhancedClient.table(userFeedTableName, TableSchema.fromClass(FeedEntity.class));
    }

    private DynamoDbTable<FeedEntity> globalFeedTable() {
        return enhancedClient.table(globalFeedTableName, TableSchema.fromClass(FeedEntity.class));
    }

    @Override
    public void putUserFeedItem(FeedEntity entity) {
        userFeedTable().putItem(entity);
    }

    @Override
    public void putGlobalFeedItem(FeedEntity entity) {
        globalFeedTable().putItem(entity);
    }

    @Override
    public PageResult<FeedEntity> getUserFeed(String userId, int limit, Map<String, AttributeValue> lastEvaluatedKey) {
        return fetchFeed(userFeedTable(), "USER#" + userId, limit, lastEvaluatedKey);
    }

    @Override
    public PageResult<FeedEntity> getGlobalFeed(int limit, Map<String, AttributeValue> lastEvaluatedKey) {
        return fetchFeed(globalFeedTable(), "GLOBAL", limit, lastEvaluatedKey);
    }

    private PageResult<FeedEntity> fetchFeed(DynamoDbTable<FeedEntity> table,
                                             String partitionKey,
                                             int limit,
                                             Map<String, AttributeValue> lastEvaluatedKey) {

        PageIterable<FeedEntity> pages = table.query(r -> r
                .queryConditional(QueryConditional.keyEqualTo(k -> k.partitionValue(partitionKey)))
                .limit(limit)
                .exclusiveStartKey(lastEvaluatedKey)
                .scanIndexForward(false)
        );

        Page<FeedEntity> firstPage = pages.iterator().next();

        return new PageResult<>(
                firstPage.items(),
                firstPage.lastEvaluatedKey()
        );
    }
}
