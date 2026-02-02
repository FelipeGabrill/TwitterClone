package com.felipegabrill.twitter.tweet_service.service.tweet.impl;

import com.felipegabrill.twitter.tweet_service.dtos.tweet.*;
import com.felipegabrill.twitter.tweet_service.dtos.tweet.response.*;
import com.felipegabrill.twitter.tweet_service.mapper.TweetMapper;
import com.felipegabrill.twitter.tweet_service.database.model.Tweet;
import com.felipegabrill.twitter.tweet_service.database.model.enums.TweetType;
import com.felipegabrill.twitter.tweet_service.database.repository.TweetRepository;
import com.felipegabrill.twitter.tweet_service.service.tweet.IHashtagService;
import com.felipegabrill.twitter.tweet_service.service.tweet.ITweetService;
import com.felipegabrill.twitter.tweet_service.service.tweet.IUserMentionService;
import com.felipegabrill.twitter.tweet_service.service.tweet.exceptions.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
public class TweetServiceImpl implements ITweetService {

    private final TweetRepository tweetRepository;
    private final TweetMapper tweetMapper;
    private final IHashtagService hashtagService;
    private final IUserMentionService userMentionService;

    public TweetServiceImpl(TweetRepository tweetRepository,
                            TweetMapper tweetMapper, IHashtagService hashtagService, IUserMentionService userMentionService) {
        this.tweetRepository = tweetRepository;
        this.tweetMapper = tweetMapper;
        this.hashtagService = hashtagService;
        this.userMentionService = userMentionService;
    }

    @Override
    @Transactional
    public NormalTweetResponseDTO createTweet(UUID authorId, CreateTweetDTO dto) {


        Tweet tweet = tweetMapper.fromCreateDTO(dto, authorId);

        initNewTweet(tweet, TweetType.NORMAL);

        processHashtagsAndMentions(tweet, dto);

        tweetRepository.save(tweet);
        return tweetMapper.toNormalResponse(tweet);
    }

    @Override
    @Transactional
    public ReplyTweetResponseDTO replyTweet(UUID authorId, ReplyTweetDTO dto) {

        Tweet parent = tweetRepository.findById(dto.getReplyToTweetId())
                .orElseThrow(() -> new ResourceNotFoundException("Tweet not found"));

        UUID rootTweetId = parent.getRootTweetId() != null
                ? parent.getRootTweetId()
                : parent.getId();

        Tweet reply = tweetMapper.fromReplyDTO(dto, authorId);

        processHashtagsAndMentions(reply, dto);

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
                .orElseThrow(() -> new ResourceNotFoundException("Tweet not found"));

        Tweet tweetToIncrement;
        UUID retweetOfId;
        UUID rootId;

        if (tweet.getType() == TweetType.RETWEET) {
            rootId = tweet.getRootTweetId() != null ? tweet.getRootTweetId() : tweet.getId();
            Tweet rootTweet = tweetRepository.findById(rootId)
                    .orElseThrow(() -> new ResourceNotFoundException("Root tweet not found"));

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
                .orElseThrow(() -> new ResourceNotFoundException("Tweet not found"));

        Tweet rootTweet = getRootTweetAndIncrementCounter(quotedTweet);

        Tweet quote = tweetMapper.fromQuoteDTO(dto, authorId);

        processHashtagsAndMentions(quote, dto);

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
                .orElseThrow(() -> new ResourceNotFoundException("Tweet not found"));

        if (!tweet.getAuthorId().equals(authorId)) {
            throw new UnauthorizedActionException("You cannot delete this tweet");
        }

        if (Boolean.TRUE.equals(tweet.getDeleted())) {
            throw new TweetDeletedException("Tweet deleted");
        }

        tweet.setDeleted(true);
        tweet.setDeletedAt(Instant.now());

        tweetRepository.save(tweet);
    }

    @Override
    @Transactional(readOnly = true)
    public BaseTweetResponseDTO getTweetById(UUID tweetId) {

        Tweet tweet = tweetRepository.findById(tweetId)
                .orElseThrow(() -> new ResourceNotFoundException("Tweet not found"));

        if (Boolean.TRUE.equals(tweet.getDeleted())) {
            throw new TweetDeletedException("Tweet deleted");
        }

        return switch (tweet.getType()) {
            case NORMAL -> tweetMapper.toNormalResponse(tweet);
            case REPLY -> tweetMapper.toReplyResponse(tweet);
            case RETWEET -> tweetMapper.toRetweetResponse(tweet);
            case QUOTE -> tweetMapper.toQuoteResponse(tweet);
            default -> throw new UnknownTweetTypeException("Unknown tweet type");
        };
    }

    @Override
    @Transactional
    public void likeTweet(UUID authorId, UUID tweetId) {
        if (!tweetRepository.existsById(tweetId)) {
            throw new ResourceNotFoundException("Tweet not found");
        }
        tweetRepository.incrementLikeCount(tweetId);
    }

    @Override
    @Transactional
    public void unlikeTweet(UUID authorId, UUID tweetId) {
        if (!tweetRepository.existsById(tweetId)) {
            throw new ResourceNotFoundException("Tweet not found");
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
                .orElseThrow(() -> new ResourceNotFoundException("Root tweet not found"));

        rootTweet.setRetweetCount(rootTweet.getRetweetCount() + 1);

        tweetRepository.save(rootTweet);

        return rootTweet;
    }

    private void processHashtagsAndMentions(Tweet tweet, TweetWithEntities dto) {
        hashtagService.validateHashtags(dto.getHashtags());
        hashtagService.attachHashtagsToTweet(tweet, dto.getHashtags());

        userMentionService.validateMentions(dto.getUserMentions());
        userMentionService.attachMentionsToTweet(tweet, dto.getUserMentions());
    }
}
