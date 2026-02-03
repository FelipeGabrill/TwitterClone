package com.felipegabrill.twitter.tweet_service.dtos.tweet;

import com.felipegabrill.twitter.tweet_service.dtos.hashtag.HashtagDTO;
import com.felipegabrill.twitter.tweet_service.dtos.usermention.UserMentionDTO;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Schema(
        name = "CreateTweetDTO",
        description = "DTO used to create a normal tweet. A tweet must contain at least text content or media."
)
public class CreateTweetDTO implements TweetWithEntities {

    @Schema(
            description = "Text content of the tweet",
            example = "Hello Twitter!",
            minLength = 1,
            maxLength = 280
    )
    @Size(
            min = 1,
            max = 280,
            message = "Content must be between 1 and 280 characters"
    )
    private String content;

    @Schema(hidden = true)
    @Size(
            max = 4,
            message = "A tweet can contain at most 4 media files"
    )
    private List<MultipartFile> media;

    @Valid
    @Size(
            max = 5,
            message = "A tweet can contain at most 5 hashtags"
    )
    private List<HashtagDTO> hashtags;

    @Valid
    @Size(
            max = 5,
            message = "A tweet can contain at most 5 user mentions"
    )
    private List<UserMentionDTO> userMentions;

    public CreateTweetDTO() {
    }

    public CreateTweetDTO(
            String content,
            List<MultipartFile> media,
            List<HashtagDTO> hashtags,
            List<UserMentionDTO> userMentions
    ) {
        this.content = content;
        this.media = media;
        this.hashtags = hashtags;
        this.userMentions = userMentions;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<MultipartFile> getMedia() {
        return media;
    }

    public void setMedia(List<MultipartFile> media) {
        this.media = media;
    }

    @Override
    public List<HashtagDTO> getHashtags() {
        return hashtags;
    }

    public void setHashtags(List<HashtagDTO> hashtags) {
        this.hashtags = hashtags;
    }

    @Override
    public List<UserMentionDTO> getUserMentions() {
        return userMentions;
    }

    public void setUserMentions(List<UserMentionDTO> userMentions) {
        this.userMentions = userMentions;
    }
}
