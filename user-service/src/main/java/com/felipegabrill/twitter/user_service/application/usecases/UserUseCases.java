package com.felipegabrill.twitter.user_service.application.usecases;

import com.felipegabrill.twitter.user_service.application.commands.CreateUserCommand;
import com.felipegabrill.twitter.user_service.application.commands.UpdateUserCommand;
import com.felipegabrill.twitter.user_service.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface UserUseCases {

    User create(CreateUserCommand command);

    User getByUsername(String username);

    User getById(UUID id);

    User updateProfile(UpdateUserCommand command);

    void deactivate(UUID userId);

    void incrementFollowersCount(UUID userId);

    void decrementFollowersCount(UUID userId);

    void incrementFollowingCount(UUID userId);

    void decrementFollowingCount(UUID userId);

    Page<User> searchUsers(String username, String name, Pageable pageable);

}
