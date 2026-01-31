package com.felipegabrill.twitter.user_service.adapters.inbound.dtos;

import com.felipegabrill.twitter.user_service.domain.user.User;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(
        name = "UserResponseDTO",
        description = "DTO representing public user profile information"
)
public class UserResponseDTO {

    @Schema(description = "Unique identifier of the user", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    private UUID id;

    @Schema(description = "Unique username", example = "felipegabrill")
    private String username;

    @Schema(description = "Display name of the user", example = "Felipe Gabrill")
    private String name;

    @Schema(description = "User biography", example = "Backend engineer passionate about distributed systems")
    private String bio;

    @Schema(description = "URL of the user's profile image")
    private String profileImageUrl;

    @Schema(description = "URL of the user's banner image")
    private String bannerImageUrl;

    @Schema(description = "User location", example = "São Paulo, Brazil")
    private String location;

    @Schema(description = "Number of followers", example = "120")
    private Integer followersCount;

    @Schema(description = "Number of users this user is following", example = "85")
    private Integer followingCount;

    @Schema(description = "Number of tweets published by the user", example = "340")
    private Integer tweetsCount;

    @Schema(description = "Indicates whether the account is private", example = "false")
    private Boolean isPrivate;

    @Schema(description = "Indicates whether the account is active", example = "true")
    private Boolean active;

    @Schema(description = "Account creation timestamp (UTC)", example = "2024-01-15T10:30:00")
    private LocalDateTime createdAt;

    @Schema(description = "Last account update timestamp (UTC)", example = "2024-02-01T18:45:00")
    private LocalDateTime updatedAt;

    public UserResponseDTO() {}

    public UserResponseDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.name = user.getName();
        this.bio = user.getBio();
        this.profileImageUrl = user.getProfileImageUrl();
        this.bannerImageUrl = user.getBannerImageUrl();
        this.location = user.getLocation();
        this.followersCount = user.getFollowersCount();
        this.followingCount = user.getFollowingCount();
        this.tweetsCount = user.getTweetsCount();
        this.isPrivate = user.isPrivate();
        this.active = user.isActive();
        this.createdAt = user.getCreatedAt();
        this.updatedAt = user.getUpdatedAt();
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public String getProfileImageUrl() { return profileImageUrl; }
    public void setProfileImageUrl(String profileImageUrl) { this.profileImageUrl = profileImageUrl; }

    public String getBannerImageUrl() { return bannerImageUrl; }
    public void setBannerImageUrl(String bannerImageUrl) { this.bannerImageUrl = bannerImageUrl; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public Integer getFollowersCount() { return followersCount; }
    public void setFollowersCount(Integer followersCount) { this.followersCount = followersCount; }

    public Integer getFollowingCount() { return followingCount; }
    public void setFollowingCount(Integer followingCount) { this.followingCount = followingCount; }

    public Integer getTweetsCount() { return tweetsCount; }
    public void setTweetsCount(Integer tweetsCount) { this.tweetsCount = tweetsCount; }

    public Boolean getPrivate() { return isPrivate; }
    public void setPrivate(Boolean aPrivate) { isPrivate = aPrivate; }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
