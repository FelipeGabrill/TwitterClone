package com.felipegabrill.twitter.user_service.domain.user.repository;

import com.felipegabrill.twitter.user_service.domain.user.User;
import com.felipegabrill.twitter.user_service.domain.user.projections.UserPreviewProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

    User save(User user);
    Optional<User> findById(UUID id);
    Optional<User> findByUsername(String username);
    Page<UserPreviewProjection> findByUsernameContainingIgnoreCaseOrNameContainingIgnoreCase(String username, String name, Pageable pageable);

}
