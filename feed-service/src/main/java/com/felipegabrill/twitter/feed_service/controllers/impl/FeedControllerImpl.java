package com.felipegabrill.twitter.feed_service.controllers.impl;

import com.felipegabrill.twitter.feed_service.controllers.IFeedController;
import com.felipegabrill.twitter.feed_service.dtos.response.FeedResponseDTO;
import com.felipegabrill.twitter.feed_service.services.IFeedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/feed")
public class FeedControllerImpl implements IFeedController {

    private static final Logger logger = LoggerFactory.getLogger(FeedControllerImpl.class);

    private final IFeedService feedService;

    public FeedControllerImpl(IFeedService feedService) {
        this.feedService = feedService;
    }

    @GetMapping("/user/{userId}")
    @Override
    public FeedResponseDTO getUserFeed(
            @PathVariable String userId,
            @RequestParam(defaultValue = "20") Integer limit,
            @RequestParam(required = false) String lastRdsKey,
            @RequestParam(required = false) String lastDynamoKey
    ) {
        logger.info(
                "HTTP GET /feed/user requested. userId={}, limit={}",
                userId, limit
        );

        if (lastRdsKey != null || lastDynamoKey != null) {
            logger.debug(
                    "Pagination cursor(s) provided for user feed. userId={}, lastRdsKey={}, lastDynamoKey={}",
                    userId, lastRdsKey, lastDynamoKey
            );
        }

        FeedResponseDTO response = feedService.getUserFeed(userId, limit, lastRdsKey, lastDynamoKey);

        logger.info(
                "User feed retrieved successfully. userId={}, itemsCount={}",
                userId, response.getItems().size()
        );

        return response;
    }


    @GetMapping("/global")
    @Override
    public FeedResponseDTO getGlobalFeed(
            @RequestParam(defaultValue = "20") Integer limit,
            @RequestParam(required = false) String lastKey
    ) {
        logger.info(
                "HTTP GET /feed/global requested. limit={}",
                limit
        );

        if (lastKey != null) {
            logger.debug("Pagination cursor provided for global feed");
        }

        FeedResponseDTO response = feedService.getGlobalFeed(limit, lastKey);

        logger.info(
                "Global feed retrieved successfully. itemsCount={}",
                response.getItems().size()
        );

        return response;
    }
}
