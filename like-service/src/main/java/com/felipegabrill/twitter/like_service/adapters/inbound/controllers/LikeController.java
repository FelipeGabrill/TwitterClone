package com.felipegabrill.twitter.like_service.adapters.inbound.controllers;

import com.felipegabrill.twitter.like_service.adapters.inbound.dtos.response.LikeResponseDTO;
import com.felipegabrill.twitter.like_service.adapters.inbound.dtos.response.LikeStatusResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(
        name = "Likes",
        description = "Endpoints responsible for managing likes on tweets"
)
@RequestMapping("/api/v1/likes")
public interface LikeController {

    @Operation(
            summary = "Like a tweet",
            description = "Registers a like from a user on a specific tweet"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Tweet liked successfully"),
            @ApiResponse(responseCode = "409", description = "User already liked this tweet"),
            @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    @PostMapping("/users/{userId}/tweets/{tweetId}")
    ResponseEntity<Void> like(
            @Parameter(
                    description = "ID of the user performing the like",
                    example = "123e4567-e89b-12d3-a456-426614174000",
                    required = true
            )
            @PathVariable UUID userId,

            @Parameter(
                    description = "ID of the tweet to be liked",
                    example = "987e6543-e21b-12d3-a456-426614174111",
                    required = true
            )
            @PathVariable UUID tweetId
    );

    @Operation(
            summary = "Unlike a tweet",
            description = "Removes a like from a user on a specific tweet"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Like removed successfully"),
            @ApiResponse(responseCode = "404", description = "Like not found"),
            @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    @DeleteMapping("/users/{userId}/tweets/{tweetId}")
    ResponseEntity<Void> unlike(
            @Parameter(
                    description = "ID of the user performing the unlike",
                    example = "123e4567-e89b-12d3-a456-426614174000",
                    required = true
            )
            @PathVariable UUID userId,

            @Parameter(
                    description = "ID of the tweet to be unliked",
                    example = "987e6543-e21b-12d3-a456-426614174111",
                    required = true
            )
            @PathVariable UUID tweetId
    );

    @Operation(
            summary = "Check if user liked a tweet",
            description = "Returns whether a given user has liked a specific tweet"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Like status retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    @GetMapping("/users/{userId}/tweets/{tweetId}/has-liked")
    ResponseEntity<LikeStatusResponseDTO> hasLiked(
            @Parameter(
                    description = "ID of the user",
                    example = "123e4567-e89b-12d3-a456-426614174000",
                    required = true
            )
            @PathVariable UUID userId,

            @Parameter(
                    description = "ID of the tweet",
                    example = "987e6543-e21b-12d3-a456-426614174111",
                    required = true
            )
            @PathVariable UUID tweetId
    );

    @Operation(
            summary = "List likes by tweet",
            description = "Returns a paginated list of users who liked a given tweet"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Likes retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    @GetMapping("/tweets/{tweetId}")
    ResponseEntity<Page<LikeResponseDTO>> listLikesByTweet(
            @Parameter(
                    description = "ID of the tweet",
                    example = "987e6543-e21b-12d3-a456-426614174111",
                    required = true
            )
            @PathVariable UUID tweetId,

            @Parameter(hidden = true)
            Pageable pageable
    );
}
