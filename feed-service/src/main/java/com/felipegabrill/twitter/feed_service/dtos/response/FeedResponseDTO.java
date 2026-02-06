package com.felipegabrill.twitter.feed_service.dtos.response;

import com.felipegabrill.twitter.feed_service.dtos.FeedItemDTO;

import java.util.List;

public class FeedResponseDTO {

    private List<FeedItemDTO> items;
    private String nextCursor;

    public FeedResponseDTO() {
    }

    public FeedResponseDTO(List<FeedItemDTO> items, String nextCursor) {
        this.items = items;
        this.nextCursor = nextCursor;
    }

    public List<FeedItemDTO> getItems() {
        return items;
    }

    public void setItems(List<FeedItemDTO> items) {
        this.items = items;
    }

    public String getNextCursor() {
        return nextCursor;
    }

    public void setNextCursor(String nextCursor) {
        this.nextCursor = nextCursor;
    }
}
