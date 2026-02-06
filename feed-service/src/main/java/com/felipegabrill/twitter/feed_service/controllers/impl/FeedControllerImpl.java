package com.felipegabrill.twitter.feed_service.controllers.impl;

import com.felipegabrill.twitter.feed_service.controllers.FeedController;
import com.felipegabrill.twitter.feed_service.dtos.response.FeedResponseDTO;
import com.felipegabrill.twitter.feed_service.services.IFeedService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/feed")
public class FeedControllerImpl implements FeedController {

    private final IFeedService feedUseCases;

    public FeedControllerImpl(IFeedService feedUseCases) {
        this.feedUseCases = feedUseCases;
    }

    @GetMapping("/user/{userId}")
    @Override
    public FeedResponseDTO getUserFeed(
            @PathVariable String userId,
            @RequestParam Integer limit,
            @RequestParam(required = false) String lastKey
    ) {
        return feedUseCases.getUserFeed(userId, limit, lastKey);
    }

    @GetMapping("/global")
    @Override
    public FeedResponseDTO getGlobalFeed(
            @RequestParam Integer limit,
            @RequestParam(required = false) String lastKey
    ) {
        return feedUseCases.getGlobalFeed(limit, lastKey);
    }
}
