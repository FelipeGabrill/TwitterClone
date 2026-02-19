package com.felipegabrill.twitter.tweet_service.service.tweet;

import com.felipegabrill.twitter.tweet_service.dtos.tweet.CreateTweetDTO;
import com.felipegabrill.twitter.tweet_service.dtos.tweet.QuoteTweetDTO;
import com.felipegabrill.twitter.tweet_service.dtos.tweet.ReplyTweetDTO;
import com.felipegabrill.twitter.tweet_service.dtos.tweet.RetweetDTO;
import com.felipegabrill.twitter.tweet_service.dtos.tweet.response.*;

import java.util.UUID;

public interface ITweetService {

    /**
     * Creates a new normal tweet for a given author.
     *
     * @param authorId the ID of the user creating the tweet
     * @param dto      the data required to create the tweet
     * @return a NormalTweetResponseDTO representing the created tweet
     */
    NormalTweetResponseDTO createTweet(UUID authorId, CreateTweetDTO dto);

    /**
     * Creates a reply to an existing tweet.
     *
     * @param authorId the ID of the user replying
     * @param dto      the data required to create the reply
     * @return a ReplyTweetResponseDTO representing the created reply
     */
    ReplyTweetResponseDTO replyTweet(UUID authorId, ReplyTweetDTO dto);

    /**
     * Retweets an existing tweet on behalf of a user.
     *
     * @param authorId the ID of the user retweeting
     * @param dto      the data required to perform the retweet
     * @return a RetweetResponseDTO representing the retweet
     */
    RetweetResponseDTO retweet(UUID authorId, RetweetDTO dto);

    /**
     * Deletes a tweet authored by a given user.
     *
     * @param authorId the ID of the user who authored the tweet
     * @param tweetId  the ID of the tweet to delete
     */
    void deleteTweet(UUID authorId, UUID tweetId);

    /**
     * Retrieves a tweet by its unique ID.
     *
     * @param tweetId the ID of the tweet to retrieve
     * @return a BaseTweetResponseDTO representing the tweet
     */
    BaseTweetResponseDTO getTweetById(UUID tweetId);

    /**
     * Creates a quote tweet for an existing tweet.
     *
     * @param authorId the ID of the user quoting the tweet
     * @param dto      the data required to create the quote tweet
     * @return a QuoteTweetResponseDTO representing the quote tweet
     */
    QuoteTweetResponseDTO quoteTweet(UUID authorId, QuoteTweetDTO dto);

    /**
     * Likes a tweet.
     *
     * @param tweetId the ID of the tweet to like
     */
    void likeTweet(UUID tweetId);

    /**
     * Removes a like from a tweet.
     *
     * @param tweetId the ID of the tweet to unlike
     */
    void unlikeTweet(UUID tweetId);
}
