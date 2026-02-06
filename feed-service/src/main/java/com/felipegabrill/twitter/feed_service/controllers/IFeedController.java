package com.felipegabrill.twitter.feed_service.controllers;

import com.felipegabrill.twitter.feed_service.dtos.response.FeedResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(
        name = "Feed",
        description = "Operations related to user and global feeds"
)
@RequestMapping("/api/v1/feed")
public interface IFeedController {

    @Operation(
            summary = "Get user feed",
            description = "Returns a paginated feed for a specific user"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Feed retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/user/{userId}")
    FeedResponseDTO getUserFeed(
            @Parameter(description = "User identifier", required = true)
            @PathVariable String userId,

            @Parameter(description = "Maximum number of items to return", example = "20")
            @RequestParam(defaultValue = "20") Integer limit,

            @Parameter(description = "Pagination cursor returned from the previous request")
            @RequestParam(required = false) String lastKey
    );

    @Operation(
            summary = "Get global feed",
            description = "Returns a paginated global feed"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Feed retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/global")
    FeedResponseDTO getGlobalFeed(
            @Parameter(description = "Maximum number of items to return", example = "20")
            @RequestParam(defaultValue = "20") Integer limit,

            @Parameter(description = "Pagination cursor returned from the previous request")
            @RequestParam(required = false) String lastKey
    );
}
