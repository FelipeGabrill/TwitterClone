package com.felipegabrill.twitter.user_service.application.usecases.impl;

import com.felipegabrill.twitter.user_service.application.commands.CreateUserCommand;
import com.felipegabrill.twitter.user_service.application.commands.UpdateUserCommand;
import com.felipegabrill.twitter.user_service.application.usecases.UserUseCases;
import com.felipegabrill.twitter.user_service.domain.user.User;
import com.felipegabrill.twitter.user_service.domain.user.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    public User create(CreateUserCommand command) {
        User user = new User(
                UUID.randomUUID(),
                command.username(),
                command.name(),
                command.bio(),
                command.location(),
                command.profileImageUrl(),
                command.bannerImageUrl()
        );
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    @Override
    public User getByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Transactional(readOnly = true)
    @Override
    public User getById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Transactional
    @Override
    public User updateProfile(UpdateUserCommand command) {
        User user = getById(command.userId());
        if (command.name() != null) {
            user.setName(command.name());
        }
        if (command.bio() != null) {
            user.setBio(command.bio());
        }
        if (command.location() != null) {
            user.setLocation(command.location());
        }
        if (command.profileImageUrl() != null) {
            user.setProfileImageUrl(command.profileImageUrl());
        }
        if (command.bannerImageUrl() != null) {
            user.setBannerImageUrl(command.bannerImageUrl());
        }

        user.setUpdatedAt(LocalDateTime.now());

        return userRepository.save(user);
    }

    @Transactional
    @Override
    public void deactivate(UUID userId) {
        User user = getById(userId);
        user.setActive(false);
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    @Transactional
    @Override
    public void incrementFollowersCount(UUID userId) {
        User user = getById(userId);
        user.setFollowersCount(user.getFollowersCount() + 1);
        userRepository.save(user);
    }

    @Transactional
    @Override
    public void decrementFollowersCount(UUID userId) {
        User user = getById(userId);
        user.setFollowersCount(Math.max(user.getFollowersCount() - 1, 0));
        userRepository.save(user);
    }

    @Transactional
    @Override
    public void incrementFollowingCount(UUID userId) {
        User user = getById(userId);
        user.setFollowingCount(user.getFollowingCount() + 1);
        userRepository.save(user);
    }

    @Transactional
    @Override
    public void decrementFollowingCount(UUID userId) {
        User user = getById(userId);
        user.setFollowingCount(Math.max(user.getFollowingCount() - 1, 0));
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<User> searchUsers(String username, String name, Pageable pageable) {
        return userRepository.findByUsernameContainingIgnoreCaseOrNameContainingIgnoreCase(username, name, pageable);
    }
}
