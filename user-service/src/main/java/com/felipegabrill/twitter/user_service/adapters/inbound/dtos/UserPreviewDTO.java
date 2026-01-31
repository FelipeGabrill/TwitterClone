package com.felipegabrill.twitter.user_service.adapters.inbound.dtos;

import com.felipegabrill.twitter.user_service.domain.user.projections.UserPreviewProjection;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(
        name = "UserPreviewDTO",
        description = "DTO used to return a preview of user information"
)
public class UserPreviewDTO {

    @Schema(
            description = "Unique user identifier",
            example = "550e8400-e29b-41d4-a716-446655440000"
    )
    private UUID id;

    @Schema(
            description = "User's public username",
            example = "john_doe"
    )
    private String username;

    @Schema(
            description = "URL of the user's profile image",
            example = "https://example.com/john_doe.jpg"
    )
    private String profileImageUrl;

    public UserPreviewDTO() {
    }

    public UserPreviewDTO(UUID id, String username, String profileImageUrl) {
        this.id = id;
        this.username = username;
        this.profileImageUrl = profileImageUrl;
    }

    public static UserPreviewDTO fromProjection(UserPreviewProjection projection) {
        return new UserPreviewDTO(
                projection.getId(),
                projection.getUsername(),
                projection.getProfileImageUrl()
        );
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }
}