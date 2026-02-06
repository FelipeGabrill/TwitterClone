package com.felipegabrill.twitter.feed_service.database.repositories.result;

import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.List;
import java.util.Map;

public record PageResult<T>(List<T> items, Map<String, AttributeValue> lastKey) {}
