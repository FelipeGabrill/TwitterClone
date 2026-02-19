package com.felipegabrill.twitter.tweet_service.controller.tweet.impl;

import com.felipegabrill.twitter.tweet_service.controller.tweet.ITweetController;
import com.felipegabrill.twitter.tweet_service.dtos.tweet.CreateTweetDTO;
import com.felipegabrill.twitter.tweet_service.dtos.tweet.QuoteTweetDTO;
import com.felipegabrill.twitter.tweet_service.dtos.tweet.ReplyTweetDTO;
import com.felipegabrill.twitter.tweet_service.dtos.tweet.RetweetDTO;
import com.felipegabrill.twitter.tweet_service.dtos.tweet.response.*;
import com.felipegabrill.twitter.tweet_service.service.tweet.ITweetService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class TweetControllerImpl implements ITweetController {

    private static final Logger log = LoggerFactory.getLogger(TweetControllerImpl.class);

    private final ITweetService tweetService;

    public TweetControllerImpl(ITweetService tweetService) {
        this.tweetService = tweetService;
    }

    @Override
    @PostMapping("/users/{authorId}/tweets")
    public ResponseEntity<NormalTweetResponseDTO> createTweet(
            @PathVariable UUID authorId,
            @Valid @ModelAttribute CreateTweetDTO createTweetDTO
    ) {

        log.info(
                "Create tweet request received | authorId={}",
                authorId
        );

        NormalTweetResponseDTO response =
                tweetService.createTweet(authorId, createTweetDTO);

        log.info(
                "Tweet created successfully | tweetId={} | authorId={}",
                response.getId(),
                authorId
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    @PostMapping("/users/{authorId}/tweets/reply")
    public ResponseEntity<ReplyTweetResponseDTO> replyTweet(
            @PathVariable UUID authorId,
            @Valid @ModelAttribute ReplyTweetDTO replyTweetDTO
    ) {

        log.info(
                "Reply tweet request received | authorId={} | repliedTweetId={}",
                authorId,
                replyTweetDTO.getReplyToTweetId()
        );

        ReplyTweetResponseDTO response =
                tweetService.replyTweet(authorId, replyTweetDTO);

        log.info(
                "Reply tweet created | tweetId={} | authorId={}",
                response.getId(),
                authorId
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    @PostMapping("/users/{authorId}/tweets/retweet")
    public ResponseEntity<RetweetResponseDTO> retweet(
            @PathVariable UUID authorId,
            @Valid @ModelAttribute RetweetDTO retweetDTO
    ) {

        log.info(
                "Retweet request received | authorId={} | originalTweetId={}",
                authorId,
                retweetDTO.getTweetId()
        );

        RetweetResponseDTO response =
                tweetService.retweet(authorId, retweetDTO);

        log.info(
                "Retweet created | tweetId={} | authorId={}",
                response.getId(),
                authorId
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    @PostMapping("/users/{authorId}/tweets/quote")
    public ResponseEntity<QuoteTweetResponseDTO> quoteTweet(
            @PathVariable UUID authorId,
            @Valid @ModelAttribute QuoteTweetDTO quoteTweetDTO
    ) {

        log.info(
                "Quote tweet request received | authorId={} | quotedTweetId={}",
                authorId,
                quoteTweetDTO.getTweetId()
        );

        QuoteTweetResponseDTO response =
                tweetService.quoteTweet(authorId, quoteTweetDTO);

        log.info(
                "Quote tweet created | tweetId={} | authorId={}",
                response.getId(),
                authorId
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    @GetMapping("/tweets/{tweetId}")
    public ResponseEntity<BaseTweetResponseDTO> getTweetById(
            @PathVariable UUID tweetId
    ) {

        log.info(
                "Get tweet by id request received | tweetId={}",
                tweetId
        );

        return ResponseEntity.ok(
                tweetService.getTweetById(tweetId)
        );
    }

    @Override
    @DeleteMapping("/users/{authorId}/tweets/{tweetId}")
    public ResponseEntity<Void> deleteTweet(
            @PathVariable UUID authorId,
            @PathVariable UUID tweetId
    ) {

        log.info(
                "Delete tweet request received | authorId={} | tweetId={}",
                authorId,
                tweetId
        );

        tweetService.deleteTweet(authorId, tweetId);

        log.info(
                "Tweet deleted | tweetId={} | authorId={}",
                tweetId,
                authorId
        );

        return ResponseEntity.noContent().build();
    }
}
