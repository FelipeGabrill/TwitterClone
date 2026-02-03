package com.felipegabrill.twitter.tweet_service.controller.tweet;

import com.felipegabrill.twitter.tweet_service.controller.tweet.exception.StandardError;
import com.felipegabrill.twitter.tweet_service.controller.tweet.exception.ValidationError;
import com.felipegabrill.twitter.tweet_service.dtos.tweet.CreateTweetDTO;
import com.felipegabrill.twitter.tweet_service.dtos.tweet.QuoteTweetDTO;
import com.felipegabrill.twitter.tweet_service.dtos.tweet.ReplyTweetDTO;
import com.felipegabrill.twitter.tweet_service.dtos.tweet.RetweetDTO;
import com.felipegabrill.twitter.tweet_service.dtos.tweet.response.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Tweets", description = "Endpoints responsible for tweet creation, interaction and retrieval")
public interface ITweetController {

    @Operation(summary = "Create a new tweet")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Tweet created successfully", content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = NormalTweetResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid tweet content", content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = StandardError.class))),
            @ApiResponse(responseCode = "422", description = "Validation error", content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = ValidationError.class))),
            @ApiResponse(responseCode = "404", description = "User not found", content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = StandardError.class)))
    })
    @PostMapping(value = "/users/{authorId}/tweets", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<NormalTweetResponseDTO> createTweet(
            @PathVariable UUID authorId,
            @Valid @ModelAttribute CreateTweetDTO createTweetDTO
    );

    @Operation(summary = "Reply to an existing tweet")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Reply created successfully", content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = ReplyTweetResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid reply content", content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = StandardError.class))),
            @ApiResponse(responseCode = "404", description = "Original tweet not found", content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = StandardError.class))),
            @ApiResponse(responseCode = "410", description = "Original tweet deleted", content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = StandardError.class)))
    })
    @PostMapping(value = "/users/{authorId}/tweets/reply", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<ReplyTweetResponseDTO> replyTweet(
            @PathVariable UUID authorId,
            @Valid @ModelAttribute ReplyTweetDTO replyTweetDTO
    );

    @Operation(summary = "Retweet a tweet")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Retweet created successfully", content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = RetweetResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Tweet not found", content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = StandardError.class))),
            @ApiResponse(responseCode = "403", description = "Unauthorized action", content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = StandardError.class)))
    })
    @PostMapping("/users/{authorId}/tweets/retweet")
    ResponseEntity<RetweetResponseDTO> retweet(
            @PathVariable UUID authorId,
            @Valid @ModelAttribute RetweetDTO retweetDTO
    );

    @Operation(summary = "Quote a tweet with optional content")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Quote tweet created successfully", content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = QuoteTweetResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid quote content", content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = StandardError.class))),
            @ApiResponse(responseCode = "404", description = "Tweet not found", content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = StandardError.class)))
    })
    @PostMapping(value = "/users/{authorId}/tweets/quote", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<QuoteTweetResponseDTO> quoteTweet(
            @PathVariable UUID authorId,
            @Valid @ModelAttribute QuoteTweetDTO quoteTweetDTO
    );

    @Operation(summary = "Like a tweet")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Tweet liked successfully"),
            @ApiResponse(responseCode = "404", description = "Tweet not found", content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = StandardError.class)))
    })
    @PostMapping("/users/{authorId}/tweets/{tweetId}/like")
    ResponseEntity<Void> likeTweet(@PathVariable UUID authorId, @PathVariable UUID tweetId);

    @Operation(summary = "Unlike a tweet")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Tweet unliked successfully"),
            @ApiResponse(responseCode = "404", description = "Tweet not found", content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = StandardError.class)))
    })
    @PostMapping("/users/{authorId}/tweets/{tweetId}/unlike")
    ResponseEntity<Void> unlikeTweet(@PathVariable UUID authorId, @PathVariable UUID tweetId);

    @Operation(summary = "Get tweet by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tweet found", content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = BaseTweetResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Tweet not found", content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = StandardError.class))),
            @ApiResponse(responseCode = "410", description = "Tweet deleted", content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = StandardError.class)))
    })
    @GetMapping("/tweets/{tweetId}")
    ResponseEntity<BaseTweetResponseDTO> getTweetById(@PathVariable UUID tweetId);

    @Operation(summary = "Delete a tweet")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Tweet deleted successfully"),
            @ApiResponse(responseCode = "403", description = "Unauthorized action", content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = StandardError.class))),
            @ApiResponse(responseCode = "404", description = "Tweet not found", content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = StandardError.class)))
    })
    @DeleteMapping("/users/{authorId}/tweets/{tweetId}")
    ResponseEntity<Void> deleteTweet(@PathVariable UUID authorId, @PathVariable UUID tweetId);
}
