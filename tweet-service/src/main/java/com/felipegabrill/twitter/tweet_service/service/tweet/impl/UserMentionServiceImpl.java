package com.felipegabrill.twitter.tweet_service.service.tweet.impl;

import com.felipegabrill.twitter.tweet_service.database.model.Tweet;
import com.felipegabrill.twitter.tweet_service.database.model.UserMention;
import com.felipegabrill.twitter.tweet_service.dtos.usermention.UserMentionDTO;
import com.felipegabrill.twitter.tweet_service.service.tweet.IUserMentionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserMentionServiceImpl implements IUserMentionService {

    private static final Logger log = LoggerFactory.getLogger(UserMentionServiceImpl.class);

    @Override
    public List<UserMention> createMentions(
            List<UserMentionDTO> mentions,
            Tweet tweet
    ) {

        if (mentions == null || mentions.isEmpty()) {
            log.debug(
                    "No user mentions to process | tweetId={}",
                    tweet.getId()
            );
            return new ArrayList<>();
        }

        log.debug(
                "Processing user mentions | tweetId={} | mentionCount={}",
                tweet.getId(),
                mentions.size()
        );

        return mentions.stream()
                .map(m -> new UserMention(
                        m.getScreenName(),
                        m.getUserId(),
                        m.getStartIndex(),
                        m.getEndIndex()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public void attachMentionsToTweet(
            Tweet tweet,
            List<UserMentionDTO> mentions
    ) {

        log.debug(
                "Attaching user mentions to tweet | tweetId={}",
                tweet.getId()
        );

        tweet.setUserMentions(
                createMentions(mentions, tweet)
        );
    }
}
