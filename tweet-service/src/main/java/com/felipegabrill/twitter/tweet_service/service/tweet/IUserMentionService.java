package com.felipegabrill.twitter.tweet_service.service.tweet;

import com.felipegabrill.twitter.tweet_service.database.model.Tweet;
import com.felipegabrill.twitter.tweet_service.database.model.UserMention;
import com.felipegabrill.twitter.tweet_service.dtos.usermention.UserMentionDTO;

import java.util.List;

public interface IUserMentionService {

    /**
     * Creates user mentions from the provided list of UserMentionDTOs and associates them with a tweet.
     *
     * @param mentions the list of UserMentionDTOs to create
     * @param tweet    the tweet to associate the mentions with
     * @return a list of created UserMention entities
     */
    List<UserMention> createMentions(List<UserMentionDTO> mentions, Tweet tweet);

    /**
     * Attaches existing user mentions to a tweet.
     *
     * @param tweet    the tweet to attach mentions to
     * @param mentions the list of UserMentionDTOs to attach
     */
    void attachMentionsToTweet(Tweet tweet, List<UserMentionDTO> mentions);
}
