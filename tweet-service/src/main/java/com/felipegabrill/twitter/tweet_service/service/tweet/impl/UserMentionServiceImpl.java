package com.felipegabrill.twitter.tweet_service.service.tweet.impl;

import com.felipegabrill.twitter.tweet_service.database.model.Tweet;
import com.felipegabrill.twitter.tweet_service.database.model.UserMention;
import com.felipegabrill.twitter.tweet_service.dtos.usermention.UserMentionDTO;
import com.felipegabrill.twitter.tweet_service.service.tweet.IUserMentionService;
import com.felipegabrill.twitter.tweet_service.service.tweet.exceptions.InvalidTweetException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserMentionServiceImpl implements IUserMentionService {

    @Override
    public void validateMentions(List<UserMentionDTO> mentions) {
        if (mentions != null && mentions.size() > 5) {
            throw new InvalidTweetException("A tweet cannot have more than 5 mentions");
        }
    }

    @Override
    public List<UserMention> createMentions(List<UserMentionDTO> mentions, Tweet tweet) {
        if (mentions == null) {
            return new ArrayList<>();
        }
        return mentions.stream()
                .map(m -> new UserMention(m.getScreenName(), m.getUserId(), m.getStartIndex(), m.getEndIndex()))
                .collect(Collectors.toList());
    }

    @Override
    public void attachMentionsToTweet(Tweet tweet, List<UserMentionDTO> mentions) {
        tweet.setUserMentions(createMentions(mentions, tweet));
    }
}