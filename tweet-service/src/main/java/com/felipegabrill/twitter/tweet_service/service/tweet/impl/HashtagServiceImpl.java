package com.felipegabrill.twitter.tweet_service.service.tweet.impl;

import com.felipegabrill.twitter.tweet_service.database.model.Hashtag;
import com.felipegabrill.twitter.tweet_service.database.model.Tweet;
import com.felipegabrill.twitter.tweet_service.dtos.hashtag.HashtagDTO;
import com.felipegabrill.twitter.tweet_service.service.tweet.IHashtagService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HashtagServiceImpl implements IHashtagService {

    private static final Logger log = LoggerFactory.getLogger(HashtagServiceImpl.class);

    @Override
    public List<Hashtag> createHashtags(List<HashtagDTO> hashtags, Tweet tweet) {

        if (hashtags == null || hashtags.isEmpty()) {
            log.debug(
                    "No hashtags provided for tweet creation | tweetId={}",
                    tweet != null ? tweet.getId() : "null"
            );
            return new ArrayList<>();
        }

        log.debug(
                "Creating hashtags for tweet | tweetId={} | hashtagCount={}",
                tweet != null ? tweet.getId() : "null",
                hashtags.size()
        );

        return hashtags.stream()
                .map(h -> new Hashtag(
                        h.getText(),
                        h.getStartIndex(),
                        h.getEndIndex()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public void attachHashtagsToTweet(Tweet tweet, List<HashtagDTO> hashtags) {

        if (tweet == null) {
            log.warn("Attempted to attach hashtags to null tweet");
            return;
        }

        List<Hashtag> createdHashtags = createHashtags(hashtags, tweet);
        tweet.setHashtags(createdHashtags);

        log.info(
                "Hashtags attached to tweet | tweetId={} | hashtagCount={}",
                tweet.getId(),
                createdHashtags.size()
        );
    }
}
