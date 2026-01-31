package com.felipegabrill.twitter.user_service.application.usecases.impl;

import com.felipegabrill.twitter.user_service.adapters.inbound.dtos.CreateUserDTO;
import com.felipegabrill.twitter.user_service.adapters.inbound.dtos.UpdateUserDTO;
import com.felipegabrill.twitter.user_service.adapters.inbound.dtos.UserPreviewDTO;
import com.felipegabrill.twitter.user_service.adapters.inbound.dtos.UserResponseDTO;
import com.felipegabrill.twitter.user_service.application.exceptions.ResourceNotFoundException;
import com.felipegabrill.twitter.user_service.application.exceptions.UsernameAlreadyExistsException;
import com.felipegabrill.twitter.user_service.application.usecases.S3UseCases;
import com.felipegabrill.twitter.user_service.application.usecases.UserUseCases;
import com.felipegabrill.twitter.user_service.domain.user.User;
import com.felipegabrill.twitter.user_service.domain.user.projections.UserPreviewProjection;
import com.felipegabrill.twitter.user_service.domain.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class UserUseCaseImpl implements UserUseCases {

    private final UserRepository userRepository;
    private final S3UseCases s3UseCases;

    public UserUseCaseImpl(UserRepository userRepository, S3UseCases s3UseCases) {
        this.userRepository = userRepository;
        this.s3UseCases = s3UseCases;
    }

    @Transactional
    @Override
    public UserResponseDTO create(CreateUserDTO createUserDTO) {
        validateUsernameDoesNotExist(createUserDTO.getUsername());

        User user = copyDtoToEntity(createUserDTO);
        saveProfileImage(createUserDTO.getProfileImage(), user);
        saveBannerImage(createUserDTO.getBannerImage(), user);

        user = userRepository.save(user);
        return new UserResponseDTO(user);
    }

    private void saveProfileImage(MultipartFile profileImage, User user) {
        if (profileImage == null || profileImage.isEmpty()) {
            return;
        }

        String profileImageUrl = s3UseCases.uploadFile(profileImage, "users/profile", user.getId() + "-profile.jpg");
        user.setProfileImageUrl(profileImageUrl);
    }

    private void saveBannerImage(MultipartFile bannerImage, User user) {
        if (bannerImage == null || bannerImage.isEmpty()) {
            return;
        }

        String bannerImageUrl = s3UseCases.uploadFile(bannerImage, "users/banner", user.getId() + "-banner.jpg");
        user.setBannerImageUrl(bannerImageUrl);
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

            if (Boolean.TRUE.equals(updateUserDTO.getRemoveProfileImage())) {
                removeProfileImage(user);
            } else if (updateUserDTO.getProfileImage() != null) {
                removeProfileImage(user);
                saveProfileImage(updateUserDTO.getProfileImage(), user);
            }

            if (Boolean.TRUE.equals(updateUserDTO.getRemoveBannerImage())) {
                removeBannerImage(user);
            } else if (updateUserDTO.getBannerImage() != null) {
                removeBannerImage(user);
                saveBannerImage(updateUserDTO.getBannerImage(), user);
            }

            user.setUpdatedAt(LocalDateTime.now());

            User updatedUser = userRepository.save(user);
            return new UserResponseDTO(updatedUser);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Resource Not Found");
        }
    }

    private void removeBannerImage(User user) {
        if (user.getBannerImageUrl() != null && !user.getBannerImageUrl().isEmpty()) {
            s3UseCases.deleteFile(user.getBannerImageUrl());
            user.setBannerImageUrl(null);
        }
    }

    private void removeProfileImage(User user) {
        if (user.getProfileImageUrl() != null && !user.getProfileImageUrl().isEmpty()) {
            s3UseCases.deleteFile(user.getProfileImageUrl());
            user.setProfileImageUrl(null);
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
    public Page<UserPreviewDTO> searchUsers(String username, String name, Pageable pageable) {
        Page<UserPreviewProjection> projections =  userRepository.findByActiveTrueAndUsernameContainingIgnoreCaseOrActiveTrueAndNameContainingIgnoreCase(username, name, pageable);
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
