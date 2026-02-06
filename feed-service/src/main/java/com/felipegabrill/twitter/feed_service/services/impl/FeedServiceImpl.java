package com.felipegabrill.twitter.feed_service.services.impl;

import com.felipegabrill.twitter.feed_service.database.entities.FeedEntity;
import com.felipegabrill.twitter.feed_service.database.repositories.IFeedRepository;
import com.felipegabrill.twitter.feed_service.database.repositories.result.PageResult;
import com.felipegabrill.twitter.feed_service.dtos.FeedItemDTO;
import com.felipegabrill.twitter.feed_service.dtos.response.FeedResponseDTO;
import com.felipegabrill.twitter.feed_service.infrastructure.mappers.FeedMapper;
import com.felipegabrill.twitter.feed_service.infrastructure.util.CursorUtil;
import com.felipegabrill.twitter.feed_service.services.IFeedService;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.List;
import java.util.Map;

@Service
public class FeedServiceImpl implements IFeedService {

    private final IFeedRepository feedRepository;
    private final FeedMapper mapper;

    public FeedServiceImpl(IFeedRepository feedRepository, FeedMapper mapper) {
        this.feedRepository = feedRepository;
        this.mapper = mapper;
    }

    @Override
    public FeedResponseDTO getUserFeed(String userId, Integer limit, String lastKey) {
        Map<String, AttributeValue> lastEvaluatedKey =
                lastKey != null ? CursorUtil.decode(lastKey) : null;

        PageResult<FeedEntity> pageResult =
                feedRepository.getUserFeed(userId, limit, lastEvaluatedKey);

        return buildFeedResponse(pageResult, userId);
    }

    @Override
    public FeedResponseDTO getGlobalFeed(Integer limit, String lastKey) {
        Map<String, AttributeValue> lastEvaluatedKey =
                lastKey != null ? CursorUtil.decode(lastKey) : null;

        PageResult<FeedEntity> pageResult =
                feedRepository.getGlobalFeed(limit, lastEvaluatedKey);

        return buildFeedResponse(pageResult, null);
    }

    private FeedResponseDTO buildFeedResponse(PageResult<FeedEntity> pageResult, String userId) {
        List<FeedItemDTO> itemsDto = pageResult.items().stream()
                .map(entity -> mapper.toDto(entity, userId))
                .toList();

        String nextCursor = pageResult.lastKey() != null
                ? CursorUtil.encode(pageResult.lastKey())
                : null;

        return new FeedResponseDTO(itemsDto, nextCursor);
    }
}
