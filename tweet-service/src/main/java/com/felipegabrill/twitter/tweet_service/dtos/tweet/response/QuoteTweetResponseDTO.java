package com.felipegabrill.twitter.tweet_service.dtos.tweet.response;

import com.felipegabrill.twitter.tweet_service.database.model.enums.TweetType;
import com.felipegabrill.twitter.tweet_service.dtos.hashtag.HashtagDTO;
import com.felipegabrill.twitter.tweet_service.dtos.media.MediaDTO;
import com.felipegabrill.twitter.tweet_service.dtos.usermention.UserMentionDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Schema(description = "Response DTO for a quote tweet (retweet with comment)")
public class QuoteTweetResponseDTO extends BaseTweetResponseDTO {

    @Schema(
            description = "Type of the tweet",
            example = "QUOTE"
    )
    private final TweetType type = TweetType.QUOTE;

    @Schema(
            description = "ID of the original tweet being quoted",
            example = "550e8400-e29b-41d4-a716-446655440000"
    )
    private final UUID retweetOfId;

    @Schema(
            description = "Text content added by the user when quoting the tweet",
            example = "Totally agree with this 👇"
    )
    private final String content;

    @Schema(
            description = "Media attached to the quote tweet. Can be empty or null"
    )
    private final List<MediaDTO> media;

    @Schema(
            description = "Hashtags extracted from the quote tweet content"
    )
    private final List<HashtagDTO> hashtags;

    @Schema(
            description = "Users mentioned in the quote tweet content"
    )
    private final List<UserMentionDTO> userMentions;

    @Schema(
            description = "Total number of retweets for this quote tweet",
            example = "7"
    )
    private final Integer retweetCount;

    @Schema(
            description = "Total number of replies to this quote tweet",
            example = "2"
    )
    private final Integer replyCount;

    public QuoteTweetResponseDTO(
            UUID id,
            UUID authorId,
            Integer likeCount,
            Instant createdAt,
            UUID retweetOfId,
            String content,
            List<MediaDTO> media,
            List<HashtagDTO> hashtags,
            List<UserMentionDTO> userMentions,
            Integer retweetCount,
            Integer replyCount
    ) {
        super(id, authorId, likeCount, createdAt);
        this.retweetOfId = retweetOfId;
        this.content = content;
        this.media = media;
        this.hashtags = hashtags;
        this.userMentions = userMentions;
        this.retweetCount = retweetCount;
        this.replyCount = replyCount;
    }

    public TweetType getType() {
        return type;
    }

    public UUID getRetweetOfId() {
        return retweetOfId;
    }

    public String getContent() {
        return content;
    }

    public List<MediaDTO> getMedia() {
        return media;
    }

    public List<HashtagDTO> getHashtags() {
        return hashtags;
    }

    public List<UserMentionDTO> getUserMentions() {
        return userMentions;
    }

    public Integer getRetweetCount() {
        return retweetCount;
    }

    public Integer getReplyCount() {
        return replyCount;
    }
}
