package com.felipegabrill.twitter.tweet_service.service.tweet;

import com.felipegabrill.twitter.tweet_service.dtos.tweet.CreateTweetDTO;
import com.felipegabrill.twitter.tweet_service.dtos.tweet.QuoteTweetDTO;
import com.felipegabrill.twitter.tweet_service.dtos.tweet.ReplyTweetDTO;
import com.felipegabrill.twitter.tweet_service.dtos.tweet.RetweetDTO;
import com.felipegabrill.twitter.tweet_service.dtos.tweet.response.*;

import java.util.UUID;

public interface ITweetService {

    NormalTweetResponseDTO createTweet(UUID authorId, CreateTweetDTO dto);

    ReplyTweetResponseDTO replyTweet(UUID authorId, ReplyTweetDTO dto);

    RetweetResponseDTO retweet(UUID authorId, RetweetDTO dto);

    void deleteTweet(UUID authorId, UUID tweetId);

    BaseTweetResponseDTO getTweetById(UUID tweetId);

    QuoteTweetResponseDTO quoteTweet(UUID authorId, QuoteTweetDTO dto);

    void likeTweet(UUID authorId, UUID tweetId);

    void unlikeTweet(UUID authorId, UUID tweetId);
}