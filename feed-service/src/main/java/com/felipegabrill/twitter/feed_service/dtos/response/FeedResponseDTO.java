package com.felipegabrill.twitter.feed_service.dtos.response;

import com.felipegabrill.twitter.feed_service.dtos.FeedItemDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Paginated feed response")
public class FeedResponseDTO {

    @Schema(
            description = "List of feed items",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private List<FeedItemDTO> items;

    @Schema(
            description = "Cursor used to fetch the next page of results",
            example = "eyJQSyI6IlVTRVIjMTIzIiwiU0siOiIxNzA3MjAwMDAwIn0=",
            nullable = true
    )
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
