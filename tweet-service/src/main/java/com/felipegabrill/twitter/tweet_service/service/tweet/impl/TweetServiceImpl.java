package com.felipegabrill.twitter.tweet_service.service.tweet.impl;

import com.felipegabrill.twitter.tweet_service.dtos.hashtag.HashtagDTO;
import com.felipegabrill.twitter.tweet_service.dtos.tweet.*;
import com.felipegabrill.twitter.tweet_service.dtos.tweet.response.*;
import com.felipegabrill.twitter.tweet_service.dtos.usermention.UserMentionDTO;
import com.felipegabrill.twitter.tweet_service.mapper.TweetMapper;
import com.felipegabrill.twitter.tweet_service.database.model.Hashtag;
import com.felipegabrill.twitter.tweet_service.database.model.Tweet;
import com.felipegabrill.twitter.tweet_service.database.model.enums.TweetType;
import com.felipegabrill.twitter.tweet_service.database.model.UserMention;
import com.felipegabrill.twitter.tweet_service.database.repository.TweetRepository;
import com.felipegabrill.twitter.tweet_service.service.tweet.ITweetService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TweetServiceImpl implements ITweetService {

    private final TweetRepository tweetRepository;
    private final TweetMapper tweetMapper;

    public TweetServiceImpl(TweetRepository tweetRepository,
                            TweetMapper tweetMapper) {
        this.tweetRepository = tweetRepository;
        this.tweetMapper = tweetMapper;
    }

    @Override
    @Transactional
    public NormalTweetResponseDTO createTweet(UUID authorId, CreateTweetDTO dto) {

        validateHashtagsAndMentions(dto.getHashtags(), dto.getUserMentions());

        Tweet tweet = tweetMapper.fromCreateDTO(dto, authorId);

        populateHashtagsAndMentions(tweet, dto);

        initNewTweet(tweet, TweetType.NORMAL);

        tweetRepository.save(tweet);
        return tweetMapper.toNormalResponse(tweet);
    }

    @Override
    @Transactional
    public ReplyTweetResponseDTO replyTweet(UUID authorId, ReplyTweetDTO dto) {

        Tweet parent = tweetRepository.findById(dto.getReplyToTweetId())
                .orElseThrow(() -> new RuntimeException("Tweet not found"));

        UUID rootTweetId = parent.getRootTweetId() != null
                ? parent.getRootTweetId()
                : parent.getId();

        validateHashtagsAndMentions(dto.getHashtags(), dto.getUserMentions());

        Tweet reply = tweetMapper.fromReplyDTO(dto, authorId);

        populateHashtagsAndMentions(reply, dto);

        initNewTweet(reply, TweetType.REPLY);
        reply.setReplyToId(parent.getId());
        reply.setRootTweetId(rootTweetId);

        parent.setReplyCount(parent.getReplyCount() + 1);

        tweetRepository.save(parent);
        tweetRepository.save(reply);
        return tweetMapper.toReplyResponse(reply);
    }

    @Override
    @Transactional
    public RetweetResponseDTO retweet(UUID authorId, RetweetDTO dto) {

        Tweet tweet = tweetRepository.findById(dto.getTweetId())
                .orElseThrow(() -> new RuntimeException("Tweet not found"));

        Tweet tweetToIncrement;
        UUID retweetOfId;
        UUID rootId;

        if (tweet.getType() == TweetType.RETWEET) {
            rootId = tweet.getRootTweetId() != null ? tweet.getRootTweetId() : tweet.getId();
            Tweet rootTweet = tweetRepository.findById(rootId)
                    .orElseThrow(() -> new RuntimeException("Root tweet not found"));

            tweetToIncrement = rootTweet;
            retweetOfId = rootTweet.getId();
        } else {
            tweetToIncrement = tweet;
            retweetOfId = tweet.getId();
            rootId = tweet.getRootTweetId() != null ? tweet.getRootTweetId() : tweet.getId();
        }

        tweetToIncrement.setRetweetCount(tweetToIncrement.getRetweetCount() + 1);
        tweetRepository.save(tweetToIncrement);

        Tweet retweet = tweetMapper.fromRetweetDTO(authorId);
        initNewTweet(retweet, TweetType.RETWEET);
        retweet.setRetweetOfId(retweetOfId);
        retweet.setRootTweetId(rootId);

        tweetRepository.save(retweet);
        return tweetMapper.toRetweetResponse(retweet);
    }


    @Override
    @Transactional
    public QuoteTweetResponseDTO quoteTweet(UUID authorId, QuoteTweetDTO dto) {

        Tweet quotedTweet = tweetRepository.findById(dto.getTweetId())
                .orElseThrow(() -> new RuntimeException("Tweet not found"));

        Tweet rootTweet = getRootTweetAndIncrementCounter(quotedTweet);

        validateHashtagsAndMentions(dto.getHashtags(), dto.getUserMentions());

        Tweet quote = tweetMapper.fromQuoteDTO(dto, authorId);

        populateHashtagsAndMentions(quote, dto);

        initNewTweet(quote, TweetType.QUOTE);
        quote.setRetweetOfId(quotedTweet.getId());
        quote.setRootTweetId(rootTweet.getId());

        tweetRepository.save(quote);
        return tweetMapper.toQuoteResponse(quote);
    }

    @Override
    @Transactional
    public void deleteTweet(UUID authorId, UUID tweetId) {

        Tweet tweet = tweetRepository.findById(tweetId)
                .orElseThrow(() -> new RuntimeException("Tweet not found"));

        if (!tweet.getAuthorId().equals(authorId)) {
            throw new RuntimeException("You cannot delete this tweet");
        }

        tweet.setDeleted(true);
        tweet.setDeletedAt(Instant.now());

        tweetRepository.save(tweet);
    }

    @Override
    @Transactional(readOnly = true)
    public BaseTweetResponseDTO getTweetById(UUID tweetId) {

        Tweet tweet = tweetRepository.findById(tweetId)
                .orElseThrow(() -> new RuntimeException("Tweet not found"));

        if (Boolean.TRUE.equals(tweet.getDeleted())) {
            throw new RuntimeException("Tweet deleted");
        }

        return switch (tweet.getType()) {
            case NORMAL -> tweetMapper.toNormalResponse(tweet);
            case REPLY -> tweetMapper.toReplyResponse(tweet);
            case RETWEET -> tweetMapper.toRetweetResponse(tweet);
            case QUOTE -> tweetMapper.toQuoteResponse(tweet);
            default -> throw new RuntimeException("Unknown tweet type");
        };
    }

    @Override
    @Transactional
    public void likeTweet(UUID authorId, UUID tweetId) {
        if (!tweetRepository.existsById(tweetId)) {
            throw new EntityNotFoundException("Tweet not found");
        }
        tweetRepository.incrementLikeCount(tweetId);
    }

    @Override
    @Transactional
    public void unlikeTweet(UUID authorId, UUID tweetId) {
        if (!tweetRepository.existsById(tweetId)) {
            throw new EntityNotFoundException("Tweet not found");
        }
        tweetRepository.decrementLikeCount(tweetId);
    }

    private void initNewTweet(Tweet tweet, TweetType type) {
        tweet.setId(UUID.randomUUID());
        tweet.setType(type);
        tweet.setCreatedAt(Instant.now());
        tweet.setDeleted(false);
        tweet.setLikeCount(0);
        tweet.setReplyCount(0);
        tweet.setRetweetCount(0);
    }

    private Tweet getRootTweetAndIncrementCounter(Tweet tweet) {
        UUID rootId = tweet.getRootTweetId() != null ? tweet.getRootTweetId() : tweet.getId();

        Tweet rootTweet = tweetRepository.findById(rootId)
                .orElseThrow(() -> new RuntimeException("Root tweet not found"));

        rootTweet.setRetweetCount(rootTweet.getRetweetCount() + 1);

        tweetRepository.save(rootTweet);

        return rootTweet;
    }

    private void validateHashtagsAndMentions(List<HashtagDTO> hashtags, List<UserMentionDTO> mentions) {
        if (hashtags != null && hashtags.size() > 5) {
            throw new IllegalArgumentException("A tweet cannot have more than 5 hashtags");
        }
        if (mentions != null && mentions.size() > 5) {
            throw new IllegalArgumentException("A tweet cannot have more than 5 mentions");
        }
    }

    private void populateHashtagsAndMentions(Tweet tweet, TweetWithEntities dto) {
        tweet.setHashtags(dto.getHashtags() != null
                ? dto.getHashtags().stream()
                .map(h -> new Hashtag(h.getText(), h.getStartIndex(), h.getEndIndex()))
                .collect(Collectors.toList())
                : new ArrayList<>());

        tweet.setUserMentions(dto.getUserMentions() != null
                ? dto.getUserMentions().stream()
                .map(m -> new UserMention(m.getScreenName(), m.getUserId(), m.getStartIndex(), m.getEndIndex()))
                .collect(Collectors.toList())
                : new ArrayList<>());
    }



}
