package com.felipegabrill.twitter.feed_service.infrastructure.mappers;

import com.felipegabrill.twitter.feed_service.database.entities.FeedEntity;
import com.felipegabrill.twitter.feed_service.dtos.FeedItemDTO;

import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface FeedMapper {

    default FeedItemDTO toDto(FeedEntity entity, String userId) {
        if (entity == null) {
            return null;
        }
        return new FeedItemDTO(
                entity.getTweetId(),
                entity.getAuthorId(),
                userId,
                entity.getScore(),
                entity.getCreatedAt()
        );
    }

}
