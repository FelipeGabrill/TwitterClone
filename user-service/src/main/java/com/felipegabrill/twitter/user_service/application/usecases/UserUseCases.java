package com.felipegabrill.twitter.user_service.application.usecases;

import com.felipegabrill.twitter.user_service.adapters.inbound.dtos.CreateUserDTO;
import com.felipegabrill.twitter.user_service.adapters.inbound.dtos.UpdateUserDTO;
import com.felipegabrill.twitter.user_service.adapters.inbound.dtos.UserPreviewDTO;
import com.felipegabrill.twitter.user_service.adapters.inbound.dtos.UserResponseDTO;
import com.felipegabrill.twitter.user_service.domain.user.projections.UserPreviewProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface UserUseCases {

    /**
     * Creates a new user in the system.
     *
     * @param userDTO the data required to create the user
     * @return a UserResponseDTO representing the created user
     */
    UserResponseDTO create(CreateUserDTO userDTO);

    /**
     * Retrieves a user by their username.
     *
     * @param username the username of the user to retrieve
     * @return a UserResponseDTO representing the user
     */
    UserResponseDTO getByUsername(String username);

    /**
     * Retrieves a user by their unique ID.
     *
     * @param id the unique identifier of the user
     * @return a UserResponseDTO representing the user
     */
    UserResponseDTO getById(UUID id);

    /**
     * Updates a user's profile with the provided information.
     *
     * @param id      the unique identifier of the user to update
     * @param userDTO the new data for the user's profile
     * @return a UserResponseDTO representing the updated user
     */
    UserResponseDTO updateProfile(UUID id, UpdateUserDTO userDTO);

    /**
     * Deactivates a user's account.
     *
     * @param userId the unique identifier of the user to deactivate
     */
    void deactivate(UUID userId);

    /**
     * Increments the followers count of a user by one.
     *
     * @param userId the unique identifier of the user
     */
    void incrementFollowersCount(UUID userId);

    /**
     * Decrements the followers count of a user by one.
     *
     * @param userId the unique identifier of the user
     */
    void decrementFollowersCount(UUID userId);

    /**
     * Searches for users based on username and/or name with pagination support.
     *
     * @param username the username filter (optional)
     * @param name     the name filter (optional)
     * @param pageable pagination information
     * @return a page of UserPreviewDTO representing the search results
     */
    Page<UserPreviewDTO> searchUsers(String username, String name, Pageable pageable);
}

