package com.felipegabrill.twitter.feed_service.database.repositories;

import com.felipegabrill.twitter.feed_service.database.entities.FeedEntity;
import com.felipegabrill.twitter.feed_service.database.repositories.result.PageResult;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.Map;

public interface IFeedRepository {

    void putUserFeedItem(FeedEntity entity);

    void putGlobalFeedItem(FeedEntity entity);

    PageResult<FeedEntity> getUserFeed(
            String userId,
            int limit,
            Map<String, AttributeValue> lastEvaluatedKey
    );

    PageResult<FeedEntity> getGlobalFeed(
            int limit,
            Map<String, AttributeValue> lastEvaluatedKey
    );
}