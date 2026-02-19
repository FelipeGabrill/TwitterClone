package com.felipegabrill.twitter.user_service.application.usecases.impl;

import com.felipegabrill.twitter.user_service.adapters.inbound.dtos.CreateUserDTO;
import com.felipegabrill.twitter.user_service.adapters.inbound.dtos.UpdateUserDTO;
import com.felipegabrill.twitter.user_service.adapters.inbound.dtos.UserPreviewDTO;
import com.felipegabrill.twitter.user_service.adapters.inbound.dtos.UserResponseDTO;
import com.felipegabrill.twitter.user_service.application.exceptions.ResourceNotFoundException;
import com.felipegabrill.twitter.user_service.application.exceptions.UsernameAlreadyExistsException;
import com.felipegabrill.twitter.user_service.application.usecases.aws.S3UseCases;
import com.felipegabrill.twitter.user_service.application.publisher.IUserPublisher;
import com.felipegabrill.twitter.user_service.application.usecases.UserUseCases;
import com.felipegabrill.twitter.user_service.domain.user.User;
import com.felipegabrill.twitter.user_service.domain.user.projections.UserPreviewProjection;
import com.felipegabrill.twitter.user_service.domain.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class UserUseCaseImpl implements UserUseCases {

    private static final Logger log = LoggerFactory.getLogger(UserUseCaseImpl.class);

    private final UserRepository userRepository;
    private final S3UseCases s3UseCases;
    private final IUserPublisher userPublisher;

    public UserUseCaseImpl(UserRepository userRepository, S3UseCases s3UseCases, IUserPublisher userPublisher) {
        this.userRepository = userRepository;
        this.s3UseCases = s3UseCases;
        this.userPublisher = userPublisher;
    }

    @Transactional
    @Override
    public UserResponseDTO create(CreateUserDTO createUserDTO) {
        log.info("Creating user with username={}", createUserDTO.getUsername());

        validateUsernameDoesNotExist(createUserDTO.getUsername());

        User user = copyDtoToEntity(createUserDTO);

        saveProfileImage(createUserDTO.getProfileImage(), user);
        saveBannerImage(createUserDTO.getBannerImage(), user);

        user = userRepository.save(user);

        log.info("User created successfully with id={} and username={}",
                user.getId(), user.getUsername());

        return new UserResponseDTO(user);
    }

    private void saveProfileImage(MultipartFile profileImage, User user) {
        if (profileImage == null || profileImage.isEmpty()) {
            log.debug("No profile image provided for user id={}", user.getId());
            return;
        }

        log.debug("Uploading profile image for user id={}", user.getId());
        String profileImageUrl =
                s3UseCases.uploadFile(profileImage, "users/profile", user.getId() + "-profile.jpg");

        user.setProfileImageUrl(profileImageUrl);
    }

    private void saveBannerImage(MultipartFile bannerImage, User user) {
        if (bannerImage == null || bannerImage.isEmpty()) {
            log.debug("No banner image provided for user id={}", user.getId());
            return;
        }

        log.debug("Uploading banner image for user id={}", user.getId());
        String bannerImageUrl =
                s3UseCases.uploadFile(bannerImage, "users/banner", user.getId() + "-banner.jpg");

        user.setBannerImageUrl(bannerImageUrl);
    }

    private void validateUsernameDoesNotExist(String username) {
        if (userRepository.existsByUsername(username)) {
            log.warn("Username already exists: {}", username);
            throw new UsernameAlreadyExistsException("Username: " + username + " already exits.");
        }
    }

    @Transactional(readOnly = true)
    @Override
    public UserResponseDTO getByUsername(String username) {
        log.debug("Fetching user by username={}", username);

        User user = userRepository.findByUsernameAndActiveTrue(username)
                .orElseThrow(() -> {
                    log.warn("User not found with username={}", username);
                    return new ResourceNotFoundException("User not found");
                });

        return new UserResponseDTO(user);
    }

    @Transactional(readOnly = true)
    @Override
    public UserResponseDTO getById(UUID id) {
        log.debug("Fetching user by id={}", id);
        return new UserResponseDTO(getUserActiveDomainById(id));
    }

    private User getUserActiveDomainById(UUID id) {
        return userRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> {
                    log.warn("Active user not found with id={}", id);
                    return new ResourceNotFoundException("User not found");
                });
    }

    @Transactional
    @Override
    public UserResponseDTO updateProfile(UUID id, UpdateUserDTO updateUserDTO) {
        log.info("Updating profile for user id={}", id);

        try {
            User user = getUserActiveDomainById(id);

            if (updateUserDTO.getName() != null) user.setName(updateUserDTO.getName());
            if (updateUserDTO.getBio() != null) user.setBio(updateUserDTO.getBio());
            if (updateUserDTO.getLocation() != null) user.setLocation(updateUserDTO.getLocation());

            if (Boolean.TRUE.equals(updateUserDTO.getRemoveProfileImage())) {
                log.warn("Removing profile image for user id={}", id);
                removeProfileImage(user);
            } else if (updateUserDTO.getProfileImage() != null) {
                removeProfileImage(user);
                saveProfileImage(updateUserDTO.getProfileImage(), user);
            }

            if (Boolean.TRUE.equals(updateUserDTO.getRemoveBannerImage())) {
                log.warn("Removing banner image for user id={}", id);
                removeBannerImage(user);
            } else if (updateUserDTO.getBannerImage() != null) {
                removeBannerImage(user);
                saveBannerImage(updateUserDTO.getBannerImage(), user);
            }

            user.setUpdatedAt(LocalDateTime.now());

            User updatedUser = userRepository.save(user);

            log.info("Profile updated successfully for user id={}", id);
            return new UserResponseDTO(updatedUser);

        } catch (EntityNotFoundException e) {
            log.error("Error updating profile for user id={}", id, e);
            throw new ResourceNotFoundException("Resource Not Found");
        }
    }

    private void removeBannerImage(User user) {
        if (user.getBannerImageUrl() != null && !user.getBannerImageUrl().isEmpty()) {
            log.debug("Deleting banner image from S3 for user id={}", user.getId());
            s3UseCases.deleteFile(user.getBannerImageUrl());
            user.setBannerImageUrl(null);
        }
    }

    private void removeProfileImage(User user) {
        if (user.getProfileImageUrl() != null && !user.getProfileImageUrl().isEmpty()) {
            log.debug("Deleting profile image from S3 for user id={}", user.getId());
            s3UseCases.deleteFile(user.getProfileImageUrl());
            user.setProfileImageUrl(null);
        }
    }

    @Transactional
    @Override
    public void deactivate(UUID userId) {
        log.warn("Deactivating user id={}", userId);

        User user = getUserActiveDomainById(userId);
        user.setActive(false);
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);

        log.warn("User deactivated successfully id={}", userId);
    }

    @Transactional
    @Override
    public void incrementFollowersCount(UUID userId) {
        log.debug("Incrementing followers count for user id={}", userId);

        User user = getUserActiveDomainById(userId);
        user.setFollowersCount(user.getFollowersCount() + 1);
        int newCount = user.getFollowersCount();
        userRepository.save(user);

        if (newCount == 2) {
            try {
                userPublisher.sendHighFollowersEvent(userId, newCount);
                log.info("High followers event sent for userId={}", userId);
            } catch (Exception e) {
                log.error("Failed to send high followers event for userId={}", userId, e);
            }
        }
    }

    @Transactional
    @Override
    public void decrementFollowersCount(UUID userId) {
        log.debug("Decrementing followers count for user id={}", userId);

        User user = getUserActiveDomainById(userId);
        user.setFollowersCount(Math.max(user.getFollowersCount() - 1, 0));
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<UserPreviewDTO> searchUsers(String username, String name, Pageable pageable) {
        log.debug("Searching users username={}, name={}, page={}, size={}",
                username, name, pageable.getPageNumber(), pageable.getPageSize());

        Page<UserPreviewProjection> projections =
                userRepository.findByActiveTrueAndUsernameContainingIgnoreCaseOrActiveTrueAndNameContainingIgnoreCase(
                        username, name, pageable);

        log.debug("Search result totalElements={}", projections.getTotalElements());

        return projections.map(UserPreviewDTO::fromProjection);
    }

    private User copyDtoToEntity(CreateUserDTO dto) {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setUsername(dto.getUsername());
        user.setName(dto.getName());
        user.setBio(dto.getBio());
        user.setLocation(dto.getLocation());
        user.setActive(true);
        user.setFollowersCount(0);
        user.setFollowingCount(0);
        user.setTweetsCount(0);
        user.setCreatedAt(LocalDateTime.now());
        user.setPrivate(false);
        return user;
    }
}
