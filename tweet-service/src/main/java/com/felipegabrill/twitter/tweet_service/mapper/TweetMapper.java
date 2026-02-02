package com.felipegabrill.twitter.tweet_service.mapper;

import com.felipegabrill.twitter.tweet_service.database.model.Tweet;
import com.felipegabrill.twitter.tweet_service.dtos.tweet.CreateTweetDTO;
import com.felipegabrill.twitter.tweet_service.dtos.tweet.QuoteTweetDTO;
import com.felipegabrill.twitter.tweet_service.dtos.tweet.ReplyTweetDTO;
import com.felipegabrill.twitter.tweet_service.dtos.tweet.response.NormalTweetResponseDTO;
import com.felipegabrill.twitter.tweet_service.dtos.tweet.response.QuoteTweetResponseDTO;
import com.felipegabrill.twitter.tweet_service.dtos.tweet.response.ReplyTweetResponseDTO;
import com.felipegabrill.twitter.tweet_service.dtos.tweet.response.RetweetResponseDTO;
import org.mapstruct.Mapper;


import java.util.UUID;

@Mapper(componentModel = "spring")
public interface TweetMapper {

    NormalTweetResponseDTO toNormalResponse(Tweet tweet);

    ReplyTweetResponseDTO toReplyResponse(Tweet tweet);

    RetweetResponseDTO toRetweetResponse(Tweet tweet);

    QuoteTweetResponseDTO toQuoteResponse(Tweet tweet);

    default Tweet fromCreateDTO(CreateTweetDTO dto, UUID authorId) {
        Tweet tweet = new Tweet();
        tweet.setAuthorId(authorId);
        tweet.setContent(dto.getContent());
        return tweet;
    }

    default Tweet fromReplyDTO(ReplyTweetDTO dto, UUID authorId) {
        Tweet tweet = new Tweet();
        tweet.setAuthorId(authorId);
        tweet.setContent(dto.getContent());
        return tweet;
    }

    default Tweet fromRetweetDTO(UUID authorId) {
        Tweet tweet = new Tweet();
        tweet.setAuthorId(authorId);
        return tweet;
    }

    default Tweet fromQuoteDTO(QuoteTweetDTO dto, UUID authorId) {
        Tweet tweet = new Tweet();
        tweet.setAuthorId(authorId);
        tweet.setContent(dto.getContent());
        return tweet;
    }
}
