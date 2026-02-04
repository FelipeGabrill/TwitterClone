package com.felipegabrill.twitter.follow_service.controller.follow;

import com.felipegabrill.twitter.follow_service.controller.follow.exceptions.StandardError;
import com.felipegabrill.twitter.follow_service.dtos.follow.FollowResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Follow", description = "Endpoints responsible for following and unfollowing users")
public interface IFollowController {

    @Operation(summary = "Follow a user")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User followed successfully", content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = FollowResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Cannot follow yourself", content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = StandardError.class))),
            @ApiResponse(responseCode = "409", description = "Already following this user", content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = StandardError.class))),
            @ApiResponse(responseCode = "404", description = "User not found", content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = StandardError.class)))
    })
    @PostMapping("/users/{followerId}/follow/{followingId}")
    ResponseEntity<FollowResponseDTO> followUser(
            @PathVariable UUID followerId,
            @PathVariable UUID followingId
    );

    @Operation(summary = "Unfollow a user")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "User unfollowed successfully"),
            @ApiResponse(responseCode = "404", description = "Target user not found", content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = StandardError.class))),
            @ApiResponse(responseCode = "400", description = "User is not following the target", content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = StandardError.class)))
    })
    @DeleteMapping("/users/{followerId}/unfollow/{followingId}")
    ResponseEntity<Void> unfollowUser(
            @PathVariable UUID followerId,
            @PathVariable UUID followingId
    );

    @Operation(summary = "Get the list of users that the given user is following")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Following list retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "User not found", content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = StandardError.class)))
    })
    @GetMapping("/users/{followerId}/following")
    ResponseEntity<Page<FollowResponseDTO>> getFollowing(
            @PathVariable UUID followerId,
            Pageable pageable
    );

    @Operation(summary = "Get the list of followers for the given user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Followers list retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "User not found", content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = StandardError.class)))
    })
    @GetMapping("/users/{followingId}/followers")
    ResponseEntity<Page<FollowResponseDTO>> getFollowers(
            @PathVariable UUID followingId,
            Pageable pageable
    );

    @Operation(summary = "Check if a user is following another user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Following status retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "User not found", content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = StandardError.class)))
    })
    @GetMapping("/users/{followerId}/is-following/{followingId}")
    ResponseEntity<Boolean> isFollowing(
            @PathVariable UUID followerId,
            @PathVariable UUID followingId
    );
}
