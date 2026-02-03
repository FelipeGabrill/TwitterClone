package com.felipegabrill.twitter.tweet_service.dtos.usermention;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.UUID;

@Schema(
        name = "UserMentionDTO",
        description = "Represents a user mention inside a tweet, including the mentioned user and its position in the text."
)
public class UserMentionDTO {

    @Schema(
            description = "Screen name of the mentioned user (without @)",
            example = "john_doe"
    )
    @NotBlank(message = "Screen name cannot be blank")
    private String screenName;

    @Schema(
            description = "Unique identifier of the mentioned user",
            example = "550e8400-e29b-41d4-a716-446655440000",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "User id is required")
    private UUID userId;

    @Schema(
            description = "Start index of the mention inside the tweet content",
            example = "0",
            minimum = "0",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Start index is required")
    @PositiveOrZero(message = "Start index must be zero or positive")
    private Integer startIndex;

    @Schema(
            description = "End index of the mention inside the tweet content",
            example = "8",
            minimum = "0",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "End index is required")
    @PositiveOrZero(message = "End index must be zero or positive")
    private Integer endIndex;

    public UserMentionDTO() {}

    public UserMentionDTO(
            String screenName,
            UUID userId,
            Integer startIndex,
            Integer endIndex
    ) {
        this.screenName = screenName;
        this.userId = userId;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public Integer getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(Integer startIndex) {
        this.startIndex = startIndex;
    }

    public Integer getEndIndex() {
        return endIndex;
    }

    public void setEndIndex(Integer endIndex) {
        this.endIndex = endIndex;
    }
}
