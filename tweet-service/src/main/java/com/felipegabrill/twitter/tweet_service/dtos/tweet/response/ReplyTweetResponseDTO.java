package com.felipegabrill.twitter.tweet_service.dtos.tweet.response;

import com.felipegabrill.twitter.tweet_service.database.model.enums.TweetType;
import com.felipegabrill.twitter.tweet_service.dtos.hashtag.HashtagDTO;
import com.felipegabrill.twitter.tweet_service.dtos.media.MediaDTO;
import com.felipegabrill.twitter.tweet_service.dtos.usermention.UserMentionDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Schema(description = "Response DTO for a reply tweet")
public class ReplyTweetResponseDTO extends BaseTweetResponseDTO {

    @Schema(
            description = "Type of the tweet",
            example = "REPLY"
    )
    private final TweetType type = TweetType.REPLY;

    @Schema(
            description = "ID of the tweet this reply is directly responding to",
            example = "111e8400-e29b-41d4-a716-446655440000"
    )
    private final UUID replyToId;

    @Schema(
            description = "ID of the root tweet of the conversation thread",
            example = "222e8400-e29b-41d4-a716-446655440000"
    )
    private final UUID rootTweetId;

    @Schema(
            description = "Text content of the reply",
            example = "I totally agree with you!"
    )
    private final String content;

    @Schema(
            description = "Media attached to the reply tweet. Can be empty or null"
    )
    private final List<MediaDTO> media;

    @Schema(
            description = "Hashtags extracted from the reply content"
    )
    private final List<HashtagDTO> hashtags;

    @Schema(
            description = "Users mentioned in the reply content"
    )
    private final List<UserMentionDTO> userMentions;

    @Schema(
            description = "Total number of retweets for this reply",
            example = "1"
    )
    private final Integer retweetCount;

    @Schema(
            description = "Total number of replies to this reply",
            example = "0"
    )
    private final Integer replyCount;

    public ReplyTweetResponseDTO(
            UUID id,
            UUID authorId,
            Integer likeCount,
            Instant createdAt,
            UUID replyToId,
            UUID rootTweetId,
            String content,
            List<MediaDTO> media,
            List<HashtagDTO> hashtags,
            List<UserMentionDTO> userMentions,
            Integer retweetCount,
            Integer replyCount
    ) {
        super(id, authorId, likeCount, createdAt);
        this.replyToId = replyToId;
        this.rootTweetId = rootTweetId;
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

    public UUID getReplyToId() {
        return replyToId;
    }

    public UUID getRootTweetId() {
        return rootTweetId;
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
