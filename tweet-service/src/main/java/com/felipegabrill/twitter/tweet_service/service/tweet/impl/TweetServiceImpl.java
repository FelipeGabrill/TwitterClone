package com.felipegabrill.twitter.tweet_service.service.tweet.impl;

import com.felipegabrill.twitter.tweet_service.database.model.Media;
import com.felipegabrill.twitter.tweet_service.database.model.Tweet;
import com.felipegabrill.twitter.tweet_service.database.model.enums.TweetType;
import com.felipegabrill.twitter.tweet_service.database.repository.TweetRepository;
import com.felipegabrill.twitter.tweet_service.dtos.tweet.*;
import com.felipegabrill.twitter.tweet_service.dtos.tweet.response.*;
import com.felipegabrill.twitter.tweet_service.mapper.TweetMapper;
import com.felipegabrill.twitter.tweet_service.service.aws.IS3Service;
import com.felipegabrill.twitter.tweet_service.publisher.ITweetPublisher;
import com.felipegabrill.twitter.tweet_service.service.exceptions.*;
import com.felipegabrill.twitter.tweet_service.service.tweet.IHashtagService;
import com.felipegabrill.twitter.tweet_service.service.tweet.ITweetService;
import com.felipegabrill.twitter.tweet_service.service.tweet.IUserMentionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class TweetServiceImpl implements ITweetService {

    private static final Logger log = LoggerFactory.getLogger(TweetServiceImpl.class);

    private final TweetRepository tweetRepository;
    private final TweetMapper tweetMapper;
    private final IHashtagService hashtagService;
    private final IUserMentionService userMentionService;
    private final IS3Service s3Service;
    private final ITweetPublisher tweetPublisher;

    public TweetServiceImpl(
            TweetRepository tweetRepository,
            TweetMapper tweetMapper,
            IHashtagService hashtagService,
            IUserMentionService userMentionService,
            IS3Service s3Service,
            ITweetPublisher tweetPublisher
    ) {
        this.tweetRepository = tweetRepository;
        this.tweetMapper = tweetMapper;
        this.hashtagService = hashtagService;
        this.userMentionService = userMentionService;
        this.s3Service = s3Service;
        this.tweetPublisher = tweetPublisher;
    }


    @Override
    @Transactional
    public NormalTweetResponseDTO createTweet(UUID authorId, CreateTweetDTO dto) {

        log.info("Creating tweet | authorId={}", authorId);

        validateContentOrMedia(dto.getContent(), dto.getMedia());

        Tweet tweet = tweetMapper.fromCreateDTO(dto, authorId);
        initNewTweet(tweet, TweetType.NORMAL);

        processHashtagsAndMentions(tweet, dto);
        saveTweetImages(dto.getMedia(), tweet);

        tweetRepository.save(tweet);

        tweetPublisher.sendMessage(
                tweet.getId(),
                tweet.getAuthorId(),
                tweet.getType(),
                null
        );

        log.info(
                "Tweet created successfully | tweetId={} | authorId={}",
                tweet.getId(),
                authorId
        );

        return tweetMapper.toNormalResponse(tweet);
    }


    @Override
    @Transactional
    public ReplyTweetResponseDTO replyTweet(UUID authorId, ReplyTweetDTO dto) {

        log.info(
                "Creating reply tweet | authorId={} | replyToTweetId={}",
                authorId,
                dto.getReplyToTweetId()
        );

        validateContentOrMedia(dto.getContent(), dto.getMedia());

        Tweet parent = tweetRepository.findById(dto.getReplyToTweetId())
                .orElseThrow(() -> new ResourceNotFoundException("Tweet not found"));

        UUID rootTweetId = parent.getRootTweetId() != null
                ? parent.getRootTweetId()
                : parent.getId();

        Tweet reply = tweetMapper.fromReplyDTO(dto, authorId);

        processHashtagsAndMentions(reply, dto);
        saveTweetImages(dto.getMedia(), reply);

        initNewTweet(reply, TweetType.REPLY);
        reply.setReplyToId(parent.getId());
        reply.setRootTweetId(rootTweetId);

        parent.setReplyCount(parent.getReplyCount() + 1);

        tweetRepository.save(parent);
        tweetRepository.save(reply);

        tweetPublisher.sendMessage(
                reply.getId(),
                reply.getAuthorId(),
                reply.getType(),
                reply.getRootTweetId()
        );

        log.info(
                "Reply tweet created | tweetId={} | rootTweetId={}",
                reply.getId(),
                rootTweetId
        );

        return tweetMapper.toReplyResponse(reply);
    }


    @Override
    @Transactional
    public RetweetResponseDTO retweet(UUID authorId, RetweetDTO dto) {

        log.info(
                "Creating retweet | authorId={} | originalTweetId={}",
                authorId,
                dto.getTweetId()
        );

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

        tweetPublisher.sendMessage(
                retweet.getId(),
                retweet.getAuthorId(),
                retweet.getType(),
                retweet.getRootTweetId()
        );

        log.info(
                "Retweet created | tweetId={} | rootTweetId={}",
                retweet.getId(),
                rootId
        );

        return tweetMapper.toRetweetResponse(retweet);
    }


    @Override
    @Transactional
    public QuoteTweetResponseDTO quoteTweet(UUID authorId, QuoteTweetDTO dto) {

        log.info(
                "Creating quote tweet | authorId={} | quotedTweetId={}",
                authorId,
                dto.getTweetId()
        );

        validateContentOrMedia(dto.getContent(), dto.getMedia());

        Tweet quotedTweet = tweetRepository.findById(dto.getTweetId())
                .orElseThrow(() -> new ResourceNotFoundException("Tweet not found"));

        Tweet rootTweet = getRootTweetAndIncrementCounter(quotedTweet);

        Tweet quote = tweetMapper.fromQuoteDTO(dto, authorId);

        processHashtagsAndMentions(quote, dto);
        saveTweetImages(dto.getMedia(), quote);

        initNewTweet(quote, TweetType.QUOTE);
        quote.setRetweetOfId(quotedTweet.getId());
        quote.setRootTweetId(rootTweet.getId());

        tweetRepository.save(quote);

        tweetPublisher.sendMessage(
                quote.getId(),
                quote.getAuthorId(),
                quote.getType(),
                quote.getRootTweetId()
        );

        log.info(
                "Quote tweet created | tweetId={} | rootTweetId={}",
                quote.getId(),
                rootTweet.getId()
        );

        return tweetMapper.toQuoteResponse(quote);
    }


    @Override
    @Transactional
    public void deleteTweet(UUID authorId, UUID tweetId) {

        log.info(
                "Deleting tweet | tweetId={} | authorId={}",
                tweetId,
                authorId
        );

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

        log.info("Tweet deleted | tweetId={}", tweetId);
    }


    @Override
    @Transactional(readOnly = true)
    public BaseTweetResponseDTO getTweetById(UUID tweetId) {

        log.debug("Fetching tweet | tweetId={}", tweetId);

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
    public void likeTweet(UUID tweetId) {

        log.debug("Liking tweet | tweetId={} ", tweetId);

        if (!tweetRepository.existsById(tweetId)) {
            throw new ResourceNotFoundException("Tweet not found");
        }

        tweetRepository.incrementLikeCount(tweetId);
    }

    @Override
    @Transactional
    public void unlikeTweet(UUID tweetId) {

        log.debug("Unliking tweet | tweetId={} ", tweetId);

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

    private void saveTweetImages(List<MultipartFile> medias, Tweet tweet) {

        if (medias == null || medias.isEmpty()) {
            return;
        }

        log.debug(
                "Uploading tweet media | tweetId={} | mediaCount={}",
                tweet.getId(),
                medias.size()
        );

        List<String> mediaUrls = s3Service.uploadFiles(
                medias,
                "tweets",
                tweet.getId()
        );

        for (int i = 0; i < mediaUrls.size(); i++) {
            tweet.getMedia().add(
                    new Media(mediaUrls.get(i), i + 1)
            );
        }
    }

    private Tweet getRootTweetAndIncrementCounter(Tweet tweet) {

        UUID rootId = tweet.getRootTweetId() != null
                ? tweet.getRootTweetId()
                : tweet.getId();

        Tweet rootTweet = tweetRepository.findById(rootId)
                .orElseThrow(() -> new ResourceNotFoundException("Root tweet not found"));

        rootTweet.setRetweetCount(rootTweet.getRetweetCount() + 1);
        tweetRepository.save(rootTweet);

        return rootTweet;
    }

    private void validateContentOrMedia(String content, List<MultipartFile> media) {

        boolean hasContent = content != null && !content.isBlank();
        boolean hasMedia = media != null && !media.isEmpty();

        if (!hasContent && !hasMedia) {
            throw new InvalidTweetException(
                    "Tweet must contain content or at least one media file"
            );
        }
    }

    private void processHashtagsAndMentions(Tweet tweet, TweetWithEntities dto) {
        hashtagService.attachHashtagsToTweet(tweet, dto.getHashtags());
        userMentionService.attachMentionsToTweet(tweet, dto.getUserMentions());
    }
}
