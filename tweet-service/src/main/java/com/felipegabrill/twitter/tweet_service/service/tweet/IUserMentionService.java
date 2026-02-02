package com.felipegabrill.twitter.tweet_service.service.tweet;

import com.felipegabrill.twitter.tweet_service.database.model.Tweet;
import com.felipegabrill.twitter.tweet_service.database.model.UserMention;
import com.felipegabrill.twitter.tweet_service.dtos.usermention.UserMentionDTO;

import java.util.List;

public interface IUserMentionService {

    void validateMentions(List<UserMentionDTO> mentions);

    List<UserMention> createMentions(List<UserMentionDTO> mentions, Tweet tweet);

    void attachMentionsToTweet(Tweet tweet, List<UserMentionDTO> mentions);
}
