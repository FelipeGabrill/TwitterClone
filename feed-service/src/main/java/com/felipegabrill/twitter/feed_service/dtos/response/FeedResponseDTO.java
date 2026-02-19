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
            description = "Cursor for RDS feed pagination",
            nullable = true,
            example = "eyJh..."
    )
    private String rdsCursor;

    @Schema(
            description = "Cursor for DynamoDB feed pagination",
            nullable = true,
            example = "eyJi..."
    )
    private String dynamoCursor;

    public FeedResponseDTO() {
    }

    public FeedResponseDTO(List<FeedItemDTO> items, String rdsCursor, String dynamoCursor) {
        this.items = items;
        this.rdsCursor = rdsCursor;
        this.dynamoCursor = dynamoCursor;
    }

    public List<FeedItemDTO> getItems() {
        return items;
    }

    public void setItems(List<FeedItemDTO> items) {
        this.items = items;
    }

    public String getRdsCursor() {
        return rdsCursor;
    }

    public void setRdsCursor(String rdsCursor) {
        this.rdsCursor = rdsCursor;
    }

    public String getDynamoCursor() {
        return dynamoCursor;
    }

    public void setDynamoCursor(String dynamoCursor) {
        this.dynamoCursor = dynamoCursor;
    }
}
