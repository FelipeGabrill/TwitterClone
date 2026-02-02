package com.felipegabrill.twitter.tweet_service.dtos.tweet;

import com.felipegabrill.twitter.tweet_service.dtos.hashtag.HashtagDTO;
import com.felipegabrill.twitter.tweet_service.dtos.usermention.UserMentionDTO;

import java.util.List;

public interface TweetWithEntities {
    List<HashtagDTO> getHashtags();

    List<UserMentionDTO> getUserMentions();

}
