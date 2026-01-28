package com.felipegabrill.twitter.user_service.application.usecases.impl;

import com.felipegabrill.twitter.user_service.adapters.inbound.dtos.CreateUserDTO;
import com.felipegabrill.twitter.user_service.adapters.inbound.dtos.UpdateUserDTO;
import com.felipegabrill.twitter.user_service.adapters.inbound.dtos.UserResponseDTO;
import com.felipegabrill.twitter.user_service.application.exceptions.ResourceNotFoundException;
import com.felipegabrill.twitter.user_service.application.exceptions.UsernameAlreadyExistsException;
import com.felipegabrill.twitter.user_service.application.usecases.UserUseCases;
import com.felipegabrill.twitter.user_service.domain.user.User;
import com.felipegabrill.twitter.user_service.domain.user.projections.UserPreviewProjection;
import com.felipegabrill.twitter.user_service.domain.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class UserUseCaseImpl implements UserUseCases {

    private final UserRepository userRepository;

    public UserUseCaseImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    @Override
    public UserResponseDTO create(CreateUserDTO createUserDTO) {
        validateUsernameDoesNotExist(createUserDTO.getUsername());

        User user = new User(
                null,
                createUserDTO.getUsername(),
                createUserDTO.getName(),
                createUserDTO.getBio(),
                createUserDTO.getLocation(),
                "",
                ""
        );
        user = userRepository.save(user);
        return new UserResponseDTO(user);
    }

    private void validateUsernameDoesNotExist(String username) {
        if (userRepository.existsByUsername(username)) {
            throw new UsernameAlreadyExistsException("Username: " + username + " already exits.");
        }
    }

    @Transactional(readOnly = true)
    @Override
    public UserResponseDTO getByUsername(String username) {
        User user = userRepository.findByUsernameAndActiveTrue(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return new UserResponseDTO(user);
    }


    @Transactional(readOnly = true)
    @Override
    public UserResponseDTO getById(UUID id) {
        User user = getUserActiveDomainById(id);
        return new UserResponseDTO(user);
    }


    private User getUserActiveDomainById(UUID id) {
        return userRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Transactional
    @Override
    public UserResponseDTO updateProfile(UUID id, UpdateUserDTO updateUserDTO) {
        try {
            User user = getUserActiveDomainById(id);

            if (updateUserDTO.getName() != null) {
                user.setName(updateUserDTO.getName());
            }
            if (updateUserDTO.getBio() != null) {
                user.setBio(updateUserDTO.getBio());
            }
            if (updateUserDTO.getLocation() != null) {
                user.setLocation(updateUserDTO.getLocation());
            }
//            if (updateUserDTO.getProfileImageUrl() != null) {
//                user.setProfileImageUrl(updateUserDTO.getProfileImageUrl());
//            }
//            if (updateUserDTO.getBannerImageUrl() != null) {
//                user.setBannerImageUrl(updateUserDTO.getBannerImageUrl());
//            }

            user.setUpdatedAt(LocalDateTime.now());

            User updatedUser = userRepository.save(user);
            return new UserResponseDTO(updatedUser);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Resource Not Found");
        }
    }

    @Transactional
    @Override
    public void deactivate(UUID userId) {
        User user = getUserActiveDomainById(userId);
        user.setActive(false);
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    @Transactional
    @Override
    public void incrementFollowersCount(UUID userId) {
        User user = getUserActiveDomainById(userId);
        user.setFollowersCount(user.getFollowersCount() + 1);
        userRepository.save(user);
    }

    @Transactional
    @Override
    public void decrementFollowersCount(UUID userId) {
        User user = getUserActiveDomainById(userId);
        user.setFollowersCount(Math.max(user.getFollowersCount() - 1, 0));
        userRepository.save(user);
    }


    @Transactional(readOnly = true)
    @Override
    public Page<UserPreviewProjection> searchUsers(String username, String name, Pageable pageable) {
        return userRepository.findByActiveTrueAndUsernameContainingIgnoreCaseOrActiveTrueAndNameContainingIgnoreCase(username, name, pageable);
    }
}
