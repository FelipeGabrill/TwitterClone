package com.felipegabrill.twitter.tweet_service.controller.tweet.impl;

import com.felipegabrill.twitter.tweet_service.controller.tweet.ITweetController;
import com.felipegabrill.twitter.tweet_service.dtos.tweet.CreateTweetDTO;
import com.felipegabrill.twitter.tweet_service.dtos.tweet.QuoteTweetDTO;
import com.felipegabrill.twitter.tweet_service.dtos.tweet.ReplyTweetDTO;
import com.felipegabrill.twitter.tweet_service.dtos.tweet.RetweetDTO;
import com.felipegabrill.twitter.tweet_service.dtos.tweet.response.*;
import com.felipegabrill.twitter.tweet_service.service.tweet.ITweetService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class TweetControllerImpl implements ITweetController {

    private final ITweetService tweetService;

    public TweetControllerImpl(ITweetService tweetService) {
        this.tweetService = tweetService;
    }

    @Override
    @PostMapping("/users/{authorId}/tweets")
    public ResponseEntity<NormalTweetResponseDTO> createTweet(
            @PathVariable UUID authorId,
            @ModelAttribute CreateTweetDTO createTweetDTO
    ) {
        NormalTweetResponseDTO response = tweetService.createTweet(authorId, createTweetDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    @PostMapping("/users/{authorId}/tweets/reply")
    public ResponseEntity<ReplyTweetResponseDTO> replyTweet(
            @PathVariable UUID authorId,
            @ModelAttribute ReplyTweetDTO replyTweetDTO
    ) {
        ReplyTweetResponseDTO response = tweetService.replyTweet(authorId, replyTweetDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    @PostMapping("/users/{authorId}/tweets/retweet")
    public ResponseEntity<RetweetResponseDTO> retweet(
            @PathVariable UUID authorId,
            @ModelAttribute RetweetDTO retweetDTO
    ) {
        RetweetResponseDTO response = tweetService.retweet(authorId, retweetDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    @PostMapping("/users/{authorId}/tweets/quote")
    public ResponseEntity<QuoteTweetResponseDTO> quoteTweet(
            @PathVariable UUID authorId,
            @ModelAttribute QuoteTweetDTO quoteTweetDTO
    ) {
        QuoteTweetResponseDTO response = tweetService.quoteTweet(authorId, quoteTweetDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    @PostMapping("/users/{authorId}/tweets/{tweetId}/like")
    public ResponseEntity<Void> likeTweet(
            @PathVariable UUID authorId,
            @PathVariable UUID tweetId
    ) {
        tweetService.likeTweet(authorId, tweetId);
        return ResponseEntity.noContent().build();
    }

    @Override
    @PostMapping("/users/{authorId}/tweets/{tweetId}/unlike")
    public ResponseEntity<Void> unlikeTweet(
            @PathVariable UUID authorId,
            @PathVariable UUID tweetId
    ) {
        tweetService.unlikeTweet(authorId, tweetId);
        return ResponseEntity.noContent().build();
    }

    @Override
    @GetMapping("/tweets/{tweetId}")
    public ResponseEntity<BaseTweetResponseDTO> getTweetById(
            @PathVariable UUID tweetId
    ) {
        return ResponseEntity.ok(tweetService.getTweetById(tweetId));
    }

    @Override
    @DeleteMapping("/users/{authorId}/tweets/{tweetId}")
    public ResponseEntity<Void> deleteTweet(
            @PathVariable UUID authorId,
            @PathVariable UUID tweetId
    ) {
        tweetService.deleteTweet(authorId, tweetId);
        return ResponseEntity.noContent().build();
    }
}
