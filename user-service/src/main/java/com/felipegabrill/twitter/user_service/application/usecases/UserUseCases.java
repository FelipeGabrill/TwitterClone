package com.felipegabrill.twitter.user_service.application.usecases;

import com.felipegabrill.twitter.user_service.adapters.inbound.dtos.CreateUserDTO;
import com.felipegabrill.twitter.user_service.adapters.inbound.dtos.UpdateUserDTO;
import com.felipegabrill.twitter.user_service.adapters.inbound.dtos.UserResponseDTO;
import com.felipegabrill.twitter.user_service.domain.user.projections.UserPreviewProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface UserUseCases {

    UserResponseDTO create(CreateUserDTO userDTO);

    UserResponseDTO getByUsername(String username);

    UserResponseDTO getById(UUID id);

    UserResponseDTO updateProfile(UUID id, UpdateUserDTO userDTO);

    void deactivate(UUID userId);

    void incrementFollowersCount(UUID userId);

    void decrementFollowersCount(UUID userId);

    Page<UserPreviewProjection> searchUsers(String username, String name, Pageable pageable);

}
