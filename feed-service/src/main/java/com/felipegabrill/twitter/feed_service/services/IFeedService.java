package com.felipegabrill.twitter.feed_service.services;

import com.felipegabrill.twitter.feed_service.dtos.response.FeedResponseDTO;


public interface IFeedService {

    FeedResponseDTO getUserFeed(String userId, Integer limit, String lastKey);

    FeedResponseDTO getGlobalFeed(Integer limit, String lastKey);

}
