package com.felipegabrill.twitter.user_service.adapters.inbound.dtos;

import com.felipegabrill.twitter.user_service.domain.user.User;

import java.time.LocalDateTime;
import java.util.UUID;

public class UserResponseDTO {

    private UUID id;
    private String username;
    private String name;
    private String bio;
    private String profileImageUrl;
    private String bannerImageUrl;
    private String location;
    private Integer followersCount;
    private Integer followingCount;
    private Integer tweetsCount;
    private Boolean isPrivate;
    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    public UserResponseDTO() {
    }

    public UserResponseDTO(User user) {
        id = user.getId();
        username = user.getUsername();
        name = user.getName();
        bio = user.getBio();
        profileImageUrl = user.getProfileImageUrl();
        bannerImageUrl = user.getBannerImageUrl();
        location = user.getLocation();
        followersCount = user.getFollowersCount();
        followingCount = user.getFollowingCount();
        tweetsCount = user.getTweetsCount();
        isPrivate = user.isPrivate();
        active = user.isActive();
        createdAt = user.getCreatedAt();
        updatedAt = user.getUpdatedAt();
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getBannerImageUrl() {
        return bannerImageUrl;
    }

    public void setBannerImageUrl(String bannerImageUrl) {
        this.bannerImageUrl = bannerImageUrl;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public Integer getFollowingCount() {
        return followingCount;
    }

    public void setFollowingCount(Integer followingCount) {
        this.followingCount = followingCount;
    }

    public Integer getFollowersCount() {
        return followersCount;
    }

    public void setFollowersCount(Integer followersCount) {
        this.followersCount = followersCount;
    }

    public Integer getTweetsCount() {
        return tweetsCount;
    }

    public void setTweetsCount(Integer tweetsCount) {
        this.tweetsCount = tweetsCount;
    }

    public Boolean getPrivate() {
        return isPrivate;
    }

    public void setPrivate(Boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
