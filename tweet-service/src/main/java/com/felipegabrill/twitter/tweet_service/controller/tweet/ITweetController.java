package com.felipegabrill.twitter.tweet_service.controller.tweet;

import com.felipegabrill.twitter.tweet_service.dtos.tweet.CreateTweetDTO;
import com.felipegabrill.twitter.tweet_service.dtos.tweet.QuoteTweetDTO;
import com.felipegabrill.twitter.tweet_service.dtos.tweet.ReplyTweetDTO;
import com.felipegabrill.twitter.tweet_service.dtos.tweet.RetweetDTO;
import com.felipegabrill.twitter.tweet_service.dtos.tweet.response.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

public interface ITweetController {

    @PostMapping("/users/{authorId}/tweets")
    ResponseEntity<NormalTweetResponseDTO> createTweet(
            @PathVariable UUID authorId,
            @ModelAttribute CreateTweetDTO createTweetDTO
    );

    @PostMapping("/users/{authorId}/tweets/reply")
    ResponseEntity<ReplyTweetResponseDTO> replyTweet(
            @PathVariable UUID authorId,
            @ModelAttribute ReplyTweetDTO replyTweetDTO
    );

    @PostMapping("/users/{authorId}/tweets/retweet")
    ResponseEntity<RetweetResponseDTO> retweet(
            @PathVariable UUID authorId,
            @ModelAttribute RetweetDTO retweetDTO
    );

    @PostMapping("/users/{authorId}/tweets/quote")
    ResponseEntity<QuoteTweetResponseDTO> quoteTweet(
            @PathVariable UUID authorId,
            @ModelAttribute QuoteTweetDTO quoteTweetDTO
    );

    @PostMapping("/users/{authorId}/tweets/{tweetId}/like")
    ResponseEntity<Void> likeTweet(
            @PathVariable UUID authorId,
            @PathVariable UUID tweetId
    );

    @PostMapping("/users/{authorId}/tweets/{tweetId}/unlike")
    ResponseEntity<Void> unlikeTweet(
            @PathVariable UUID authorId,
            @PathVariable UUID tweetId
    );

    @GetMapping("/tweets/{tweetId}")
    ResponseEntity<BaseTweetResponseDTO> getTweetById(
            @PathVariable UUID tweetId
    );

    @DeleteMapping("/users/{authorId}/tweets/{tweetId}")
    ResponseEntity<Void> deleteTweet(
            @PathVariable UUID authorId,
            @PathVariable UUID tweetId
    );
}
