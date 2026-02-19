package com.felipegabrill.twitter.tweet_service.publisher.impl;

import com.felipegabrill.twitter.tweet_service.database.model.enums.TweetType;
import com.felipegabrill.twitter.tweet_service.publisher.ITweetPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sns.SnsAsyncClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;

import java.time.Instant;
import java.util.UUID;

@Service
public class TweetPublisherImpl implements ITweetPublisher {

    private static final Logger logger = LoggerFactory.getLogger(TweetPublisherImpl.class);

    private final SnsAsyncClient snsClient;

    @Value("${aws.sns.tweet-event.topic-arn}")
    private String tweetTopicArn;

    public TweetPublisherImpl(SnsAsyncClient snsClient) {
        this.snsClient = snsClient;
    }

    @Override
    public void sendMessage(UUID tweetId, UUID authorId, TweetType tweetType, UUID rootTweetId) {
        String eventId = UUID.randomUUID().toString();
        String eventType = "TweetCreated";

        logger.info(
                "Publishing SNS tweet event. eventType={}, eventId={}, tweetId={}, authorId={}",
                eventType, eventId, tweetId, authorId
        );

        String payload = """
                {
                  "eventId": "%s",
                  "eventType": "%s",
                  "data": {
                    "tweetId": "%s",
                    "authorId": "%s",
                    "tweetType": "%s",
                    "rootTweetId": %s,
                    "occurredAt": "%s"
                  }
                }
                """.formatted(
                eventId,
                eventType,
                tweetId,
                authorId,
                tweetType,
                rootTweetId != null ? "\"" + rootTweetId + "\"" : "null",
                Instant.now()
        );

        snsClient.publish(
                        PublishRequest.builder()
                                .topicArn(tweetTopicArn)
                                .message(payload)
                                .build()
                )
                .thenAccept(response ->
                        logger.info(
                                "SNS tweet event published successfully. eventId={}, messageId={}",
                                eventId, response.messageId()
                        )
                )
                .exceptionally(ex -> {
                    logger.error(
                            "Failed to publish SNS tweet event. eventId={}, tweetId={}",
                            eventId, tweetId, ex
                    );
                    return null;
                });
    }
}