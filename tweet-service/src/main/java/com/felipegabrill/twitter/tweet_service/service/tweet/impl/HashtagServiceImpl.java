package com.felipegabrill.twitter.tweet_service.service.tweet.impl;

import com.felipegabrill.twitter.tweet_service.database.model.Hashtag;
import com.felipegabrill.twitter.tweet_service.database.model.Tweet;
import com.felipegabrill.twitter.tweet_service.dtos.hashtag.HashtagDTO;
import com.felipegabrill.twitter.tweet_service.service.tweet.IHashtagService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HashtagServiceImpl implements IHashtagService {

    @Override
    public List<Hashtag> createHashtags(List<HashtagDTO> hashtags, Tweet tweet) {
        if (hashtags == null) {
            return new ArrayList<>();
        }
        return hashtags.stream()
                .map(h -> new Hashtag(h.getText(), h.getStartIndex(), h.getEndIndex()))
                .collect(Collectors.toList());
    }

    @Override
    public void attachHashtagsToTweet(Tweet tweet, List<HashtagDTO> hashtags) {
        tweet.setHashtags(createHashtags(hashtags, tweet));
    }
}
