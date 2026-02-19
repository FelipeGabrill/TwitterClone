package com.felipegabrill.twitter.user_service.application.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.felipegabrill.twitter.user_service.application.consumer.events.UserFollowEvent;
import com.felipegabrill.twitter.user_service.application.exceptions.NonRetryableEventException;
import com.felipegabrill.twitter.user_service.application.usecases.UserUseCases;
import io.awspring.cloud.sqs.annotation.SqsListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class UserFollowEventConsumer {

    private static final Logger log =
            LoggerFactory.getLogger(UserFollowEventConsumer.class);

    private final ObjectMapper objectMapper;
    private final UserUseCases userUseCases;

    public UserFollowEventConsumer(
            ObjectMapper objectMapper,
            UserUseCases userUseCases
    ) {
        this.objectMapper = objectMapper;
        this.userUseCases = userUseCases;
    }

    @SqsListener("${aws.sqs.user-follow-events-queue-url}")
    public void consume(String message) {

        try {
            UserFollowEvent event = parseEvent(message);

            log.info(
                    "Processing UserFollowEvent | eventType={} | followedId={} | occurredAt={}",
                    event.eventType(),
                    event.data().followedId(),
                    event.data().occurredAt()
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

    private UserFollowEvent parseEvent(String message) {

        try {
            JsonNode snsEnvelope = objectMapper.readTree(message);
            String payload = snsEnvelope.path("Message").asText("");

            if (payload.isBlank()) {
                throw new NonRetryableEventException(
                        "SNS envelope missing or empty 'Message'"
                );
            }

            UserFollowEvent event =
                    objectMapper.readValue(payload, UserFollowEvent.class);

            validate(event);

            return event;

        } catch (JsonProcessingException e) {
            throw new NonRetryableEventException(
                    "Failed to deserialize UserFollowEvent"
            );
        }
    }

    private void validate(UserFollowEvent event) {

        if (event.eventType() == null ||
                event.data() == null ||
                event.data().followedId() == null) {

            throw new NonRetryableEventException(
                    "Missing required fields in UserFollowEvent"
            );
        }
    }

    private void handleEvent(UserFollowEvent event) {

        switch (event.eventType()) {

            case "UserFollowed" ->
                    userUseCases.incrementFollowersCount(
                            event.data().followedId()
                    );

            case "UserUnfollowed" ->
                    userUseCases.decrementFollowersCount(
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
