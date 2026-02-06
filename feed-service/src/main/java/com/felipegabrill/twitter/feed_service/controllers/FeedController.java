package com.felipegabrill.twitter.feed_service.controllers;


import com.felipegabrill.twitter.feed_service.dtos.response.FeedResponseDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/api/v1/feed")
public interface FeedController {

    @GetMapping("/user/{userId}")
    FeedResponseDTO getUserFeed(@PathVariable String userId,
                                      @RequestParam Integer limit,
                                      @RequestParam(required = false) String lastKey);

    @GetMapping("/global")
    FeedResponseDTO getGlobalFeed(
            @RequestParam Integer limit,
            @RequestParam(required = false) String lastKey
    );

}