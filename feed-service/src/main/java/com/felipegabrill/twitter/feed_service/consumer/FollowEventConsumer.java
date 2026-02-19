package com.felipegabrill.twitter.feed_service.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.felipegabrill.twitter.feed_service.consumer.events.follow.FollowEvent;
import com.felipegabrill.twitter.feed_service.consumer.exceptions.NonRetryableEventException;
import com.felipegabrill.twitter.feed_service.services.IFollowService;
import io.awspring.cloud.sqs.annotation.SqsListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class FollowEventConsumer {

    private static final Logger log =
            LoggerFactory.getLogger(FollowEventConsumer.class);

    private final ObjectMapper objectMapper;
    private final IFollowService followService;

    public FollowEventConsumer(
            ObjectMapper objectMapper,
            IFollowService followService
    ) {
        this.objectMapper = objectMapper;
        this.followService = followService;
    }

    @SqsListener("${aws.sqs.follow-events-queue-url}")
    public void consume(String message) {

        try {
            FollowEvent event = parseEvent(message);

            log.info(
                    "Processing FollowEvent | eventType={} | followerId={} | followedId={}",
                    event.eventType(),
                    event.data().followerId(),
                    event.data().followedId()
            );

            handleEvent(event);

        } catch (NonRetryableEventException ex) {
            log.error(
                    "Non-retryable error while processing message. Discarding. body={}",
                    message,
                    ex
            );
        } catch (Exception ex) {
            log.error(
                    "Retryable error while processing message. Will retry.",
                    ex
            );
            throw ex;
        }
    }

    private FollowEvent parseEvent(String message) {

        try {
            JsonNode snsEnvelope = objectMapper.readTree(message);
            String payload = snsEnvelope.path("Message").asText("");

            if (payload.isBlank()) {
                throw new NonRetryableEventException(
                        "SNS envelope missing or empty 'Message'"
                );
            }

            FollowEvent event =
                    objectMapper.readValue(payload, FollowEvent.class);

            validate(event);

            return event;

        } catch (JsonProcessingException e) {
            throw new NonRetryableEventException(
                    "Failed to deserialize FollowEvent"
            );
        }
    }

    private void validate(FollowEvent event) {

        if (event.eventType() == null ||
                event.data() == null ||
                event.data().followerId() == null ||
                event.data().followedId() == null) {

            throw new NonRetryableEventException(
                    "Missing required fields in FollowEvent"
            );
        }
    }

    private void handleEvent(FollowEvent event) {

        switch (event.eventType()) {

            case "UserFollowed" ->
                    followService.followUser(
                            event.data().followerId(),
                            event.data().followedId()
                    );

            case "UserUnfollowed" ->
                    followService.unfollowUser(
                            event.data().followerId(),
                            event.data().followedId()
                    );

            default ->
                    log.warn(
                            "Unknown eventType received: {}",
                            event.eventType()
                    );
        }
    }
}
