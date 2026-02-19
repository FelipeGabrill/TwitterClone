package com.felipegabrill.twitter.feed_service.infrastructure.mappers;

import com.felipegabrill.twitter.feed_service.database.dynamodb.entities.Feed;
import com.felipegabrill.twitter.feed_service.dtos.FeedItemDTO;

import org.mapstruct.Mapper;

import java.time.Instant;
import java.util.UUID;


@Mapper(componentModel = "spring")
public interface FeedMapper {

    default FeedItemDTO toDto(Feed entity, String userId) {
        if (entity == null) {
            return null;
        }
        return new FeedItemDTO(
                entity.getTweetId(),
                entity.getAuthorId(),
                userId,
                entity.getCreatedAt()
        );
    }

    default Feed toGlobalFeed(UUID tweetId, UUID authorId, Instant createdAt) {
        Feed feed = new Feed();
        feed.setPk("FEED#GLOBAL#FOR_YOU");
        feed.setSk("TWEET#" + tweetId);
        feed.setTweetId(tweetId.toString());
        feed.setAuthorId(authorId.toString());
        feed.setCreatedAt(createdAt);
        feed.setFeedType("FOR_YOU");
        feed.setGsiDatePk("FEED#GLOBAL#FOR_YOU");
        feed.setGsiDateSk(createdAt.toString());
        return feed;
    }

    default Feed toUserFeed(UUID tweetId, UUID authorId, String userId, Instant createdAt) {
        Feed feed = new Feed();
        feed.setPk("USER#" + userId);
        feed.setSk("TWEET#" + tweetId);
        feed.setTweetId(tweetId.toString());
        feed.setAuthorId(authorId.toString());
        feed.setCreatedAt(createdAt);
        feed.setFeedType("USER_FEED");
        feed.setGsiDatePk("USER#" + userId);
        feed.setGsiDateSk(createdAt.toString());
        return feed;
    }

}
