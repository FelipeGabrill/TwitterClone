package com.felipegabrill.twitter.like_service.application.publisher.impl;

import com.felipegabrill.twitter.like_service.application.publisher.ILikePublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sns.SnsAsyncClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;

import java.time.Instant;
import java.util.UUID;

@Service
public class LikePublisher implements ILikePublisher {

    private static final Logger log = LoggerFactory.getLogger(LikePublisher.class);

    private final SnsAsyncClient snsClient;

    @Value("${aws.sns.like-event.topic-arn}")
    private String likeTopicArn;

    public LikePublisher(SnsAsyncClient snsClient) {
        this.snsClient = snsClient;
    }

    @Override
    public void publishLikeCreated(UUID tweetId, UUID userId) {
        publishEvent("LikeCreated", tweetId, userId);
    }

    @Override
    public void publishLikeDeleted(UUID tweetId, UUID userId) {
        publishEvent("LikeDeleted", tweetId, userId);
    }

    private void publishEvent(String eventType, UUID tweetId, UUID userId) {
        log.info(
                "Publishing {} event | tweetId={} | userId={}",
                eventType, tweetId, userId
        );

        String payload = buildPayload(eventType, tweetId, userId);

        snsClient.publish(
                        PublishRequest.builder()
                                .topicArn(likeTopicArn)
                                .message(payload)
                                .messageGroupId(tweetId.toString())
                                .messageDeduplicationId(UUID.randomUUID().toString())
                                .build()
                )
                .thenAccept(response ->
                        log.info(
                                "SNS event published | eventType={} | messageId={}",
                                eventType,
                                response.messageId()
                        )
                )
                .exceptionally(ex -> {
                    log.error(
                            "Failed to publish SNS event | eventType={} | topicArn={}",
                            eventType,
                            likeTopicArn,
                            ex
                    );
                    return null;
                });
    }

    private String buildPayload(String eventType, UUID tweetId, UUID userId) {
        return String.format(
                """
                {
                  "eventType": "%s",
                  "data": {
                    "tweetId": "%s",
                    "userId": "%s",
                    "occurredAt": "%s"
                  }
                }
                """,
                eventType,
                tweetId,
                userId,
                Instant.now()
        );
    }
}
