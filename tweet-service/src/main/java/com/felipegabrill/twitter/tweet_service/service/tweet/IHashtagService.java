package com.felipegabrill.twitter.tweet_service.service.tweet;

import com.felipegabrill.twitter.tweet_service.database.model.Hashtag;
import com.felipegabrill.twitter.tweet_service.database.model.Tweet;
import com.felipegabrill.twitter.tweet_service.dtos.hashtag.HashtagDTO;

import java.util.List;

public interface IHashtagService {

    /**
     * Creates hashtags from the provided list of HashtagDTOs and associates them with a tweet.
     *
     * @param hashtags the list of HashtagDTOs to create
     * @param tweet    the tweet to associate the hashtags with
     * @return a list of created Hashtag entities
     */
    List<Hashtag> createHashtags(List<HashtagDTO> hashtags, Tweet tweet);

    /**
     * Attaches existing hashtags to a tweet.
     *
     * @param tweet    the tweet to attach hashtags to
     * @param hashtags the list of HashtagDTOs to attach
     */
    void attachHashtagsToTweet(Tweet tweet, List<HashtagDTO> hashtags);
}
