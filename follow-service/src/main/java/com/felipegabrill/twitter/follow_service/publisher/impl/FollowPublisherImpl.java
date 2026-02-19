package com.felipegabrill.twitter.follow_service.publisher.impl;

import com.felipegabrill.twitter.follow_service.publisher.IFollowPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sns.SnsAsyncClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;

import java.time.Instant;
import java.util.UUID;

@Service
public class FollowPublisherImpl implements IFollowPublisher {

    private static final Logger logger = LoggerFactory.getLogger(FollowPublisherImpl.class);

    private final SnsAsyncClient snsClient;

    @Value("${aws.sns.follow-event.topic-arn}")
    private String followEventsTopicArn;

    public FollowPublisherImpl(SnsAsyncClient snsClient) {
        this.snsClient = snsClient;
    }

    @Override
    public void publishUserFollowed(UUID followerId, UUID followedId) {
        publishEvent("UserFollowed", followerId, followedId);
    }

    @Override
    public void publishUserUnfollowed(UUID followerId, UUID followedId) {
        publishEvent("UserUnfollowed", followerId, followedId);
    }

    private void publishEvent(String eventType, UUID followerId, UUID followedId) {
        String eventId = UUID.randomUUID().toString();

        logger.info(
                "Publishing SNS follow event. eventType={}, eventId={}, followerId={}, followedId={}",
                eventType, eventId, followerId, followedId
        );

        String payload = String.format(
                """
                {
                  "eventId": "%s",
                  "eventType": "%s",
                  "data": {
                    "followerId": "%s",
                    "followedId": "%s",
                    "occurredAt": "%s"
                  }
                }
                """,
                eventId,
                eventType,
                followerId,
                followedId,
                Instant.now()
        );

        snsClient.publish(
                        PublishRequest.builder()
                                .topicArn(followEventsTopicArn)
                                .messageGroupId(followerId + "#" + followedId)
                                .messageDeduplicationId(eventId)
                                .message(payload)
                                .build()
                )
                .thenAccept(response ->
                        logger.info(
                                "SNS event published successfully. eventId={}, messageId={}",
                                eventId, response.messageId()
                        )
                )
                .exceptionally(ex -> {
                    logger.error(
                            "Failed to publish SNS follow event. eventId={}, eventType={}, followerId={}, followedId={}",
                            eventId, eventType, followerId, followedId, ex
                    );
                    return null;
                });
    }
}
