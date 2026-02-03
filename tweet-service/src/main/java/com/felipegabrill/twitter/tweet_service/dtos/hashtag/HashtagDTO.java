package com.felipegabrill.twitter.tweet_service.dtos.hashtag;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

@Schema(
        name = "HashtagDTO",
        description = "Represents a hashtag inside a tweet, including its text and position in the content."
)
public class HashtagDTO {

    @Schema(
            description = "Hashtag text without the # symbol",
            example = "SpringBoot"
    )
    @NotBlank(message = "Hashtag text must not be blank")
    @Size(
            min = 1,
            max = 100,
            message = "Hashtag text must be between 1 and 100 characters"
    )
    private String text;

    @Schema(
            description = "Start index of the hashtag inside the tweet content",
            example = "10",
            minimum = "0",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "startIndex is required")
    @PositiveOrZero(message = "startIndex must be zero or positive")
    private Integer startIndex;

    @Schema(
            description = "End index of the hashtag inside the tweet content",
            example = "21",
            minimum = "0",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "endIndex is required")
    @PositiveOrZero(message = "endIndex must be zero or positive")
    private Integer endIndex;

    public HashtagDTO() {
    }

    public HashtagDTO(
            String text,
            Integer startIndex,
            Integer endIndex
    ) {
        this.text = text;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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
