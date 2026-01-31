package com.felipegabrill.twitter.user_service.adapters.inbound.controllers;

import com.felipegabrill.twitter.user_service.adapters.inbound.dtos.CreateUserDTO;
import com.felipegabrill.twitter.user_service.adapters.inbound.dtos.UpdateUserDTO;
import com.felipegabrill.twitter.user_service.adapters.inbound.dtos.UserPreviewDTO;
import com.felipegabrill.twitter.user_service.adapters.inbound.dtos.UserResponseDTO;
import com.felipegabrill.twitter.user_service.domain.user.projections.UserPreviewProjection;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Users", description = "Endpoints responsible for user management in the Twitter Clone")
@RequestMapping("/api/v1/users")
public interface UserController {

    @Operation(
            summary = "Create a new user",
            description = "Creates a new user account with optional profile and banner images",
            responses = {
                    @ApiResponse(responseCode = "201", description = "User created successfully",
                            content = @Content(schema = @Schema(implementation = UserResponseDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input data"),
                    @ApiResponse(responseCode = "422", description = "Validation error")
            }
    )
    @PostMapping
    ResponseEntity<UserResponseDTO> insert(@Valid @ModelAttribute CreateUserDTO dto);

    @Operation(
            summary = "Get user by username",
            description = "Returns a user profile by its username",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User found",
                            content = @Content(schema = @Schema(implementation = UserResponseDTO.class))),
                    @ApiResponse(responseCode = "404", description = "User not found")
            }
    )
    @GetMapping("/username/{username}")
    ResponseEntity<UserResponseDTO> getByUsername(
            @Parameter(description = "Username of the user", required = true)
            @PathVariable String username
    );

    @Operation(
            summary = "Get user by ID",
            description = "Returns a user profile by its unique identifier",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User found",
                            content = @Content(schema = @Schema(implementation = UserResponseDTO.class))),
                    @ApiResponse(responseCode = "404", description = "User not found")
            }
    )
    @GetMapping("/{id}")
    ResponseEntity<UserResponseDTO> findById(
            @Parameter(description = "User UUID", required = true)
            @PathVariable UUID id
    );

    @Operation(
            summary = "Update user profile",
            description = "Updates user profile data, profile image and banner image",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User updated successfully",
                            content = @Content(schema = @Schema(implementation = UserResponseDTO.class))),
                    @ApiResponse(responseCode = "404", description = "User not found"),
                    @ApiResponse(responseCode = "422", description = "Validation error")
            }
    )
    @PutMapping("/{id}")
    ResponseEntity<UserResponseDTO> update(
            @Parameter(description = "User UUID", required = true)
            @PathVariable UUID id,
            @Valid @ModelAttribute UpdateUserDTO dto
    );

    @Operation(
            summary = "Deactivate user",
            description = "Soft deletes a user by marking it as inactive",
            responses = {
                    @ApiResponse(responseCode = "204", description = "User deactivated successfully"),
                    @ApiResponse(responseCode = "404", description = "User not found")
            }
    )
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deactivateUser(
            @Parameter(description = "User UUID", required = true)
            @PathVariable UUID id
    );

    @Operation(
            summary = "Follow user",
            description = "Increments the followers count of a user",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Follower count incremented"),
                    @ApiResponse(responseCode = "404", description = "User not found")
            }
    )
    @PatchMapping("/{id}/follow")
    ResponseEntity<Void> incrementFollowing(
            @Parameter(description = "User UUID", required = true)
            @PathVariable UUID id
    );

    @Operation(
            summary = "Unfollow user",
            description = "Decrements the followers count of a user",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Follower count decremented"),
                    @ApiResponse(responseCode = "404", description = "User not found")
            }
    )
    @PatchMapping("/{id}/unfollow")
    ResponseEntity<Void> decrementFollowing(
            @Parameter(description = "User UUID", required = true)
            @PathVariable UUID id
    );

    @Operation(
            summary = "Search users",
            description = "Search users by username or name with pagination support",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Users retrieved successfully")
            }
    )
    @GetMapping("/search")
    ResponseEntity<Page<UserPreviewDTO>> searchUsers(
            @Parameter(description = "Username to search") @RequestParam(required = false) String username,
            @Parameter(description = "Name to search") @RequestParam(required = false) String name,
            Pageable pageable
    );
}
