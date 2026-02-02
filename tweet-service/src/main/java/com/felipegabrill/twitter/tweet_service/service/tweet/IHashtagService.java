package com.felipegabrill.twitter.tweet_service.service.tweet;

import com.felipegabrill.twitter.tweet_service.database.model.Hashtag;
import com.felipegabrill.twitter.tweet_service.database.model.Tweet;
import com.felipegabrill.twitter.tweet_service.dtos.hashtag.HashtagDTO;

import java.util.List;

public interface IHashtagService {

    void validateHashtags(List<HashtagDTO> hashtags);

    List<Hashtag> createHashtags(List<HashtagDTO> hashtags, Tweet tweet);

    void attachHashtagsToTweet(Tweet tweet, List<HashtagDTO> hashtags);

}
