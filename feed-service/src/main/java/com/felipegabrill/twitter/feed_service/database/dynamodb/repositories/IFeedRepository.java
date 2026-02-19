package com.felipegabrill.twitter.feed_service.database.dynamodb.repositories;

import com.felipegabrill.twitter.feed_service.database.dynamodb.entities.Feed;
import com.felipegabrill.twitter.feed_service.database.dynamodb.repositories.result.PageResult;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.Map;

public interface IFeedRepository {

    void saveUserFeedItem(Feed feedItem);

    void saveGlobalFeedItem(Feed feedItem);

    PageResult<Feed> getGlobalFeedByDate(int limit, Map<String, AttributeValue> lastEvaluatedKey);

    PageResult<Feed> getUserFeedByDate(String userId, int limit, Map<String, AttributeValue> lastEvaluatedKey);
}