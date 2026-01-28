package com.felipegabrill.twitter.user_service.adapters.outbound.repositories.impl;

import com.felipegabrill.twitter.user_service.adapters.outbound.entities.JpaUserEntity;
import com.felipegabrill.twitter.user_service.adapters.outbound.repositories.JpaUserRepository;
import com.felipegabrill.twitter.user_service.domain.user.User;
import com.felipegabrill.twitter.user_service.domain.user.projections.UserPreviewProjection;
import com.felipegabrill.twitter.user_service.domain.user.repository.UserRepository;
import com.felipegabrill.twitter.user_service.infrastructure.mappers.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final JpaUserRepository jpaUserRepository;
    private final UserMapper userMapper;

    @Autowired
    public UserRepositoryImpl(JpaUserRepository jpaUserRepository, UserMapper userMapper) {
        this.jpaUserRepository = jpaUserRepository;
        this.userMapper = userMapper;
    }

    @Override
    public User save(User user) {
        JpaUserEntity entity = userMapper.toEntity(user);
        JpaUserEntity saved = jpaUserRepository.save(entity);
        return userMapper.toDomain(saved);
    }

    @Override
    public Optional<User> findByIdAndActiveTrue(UUID id) {
        return jpaUserRepository.findByIdAndActiveTrue(id).map(userMapper::toDomain);
    }

    @Override
    public Optional<User> findByUsernameAndActiveTrue(String username) {
        return jpaUserRepository.findByUsernameAndActiveTrue(username).map(userMapper::toDomain);
    }

    @Override
    public Page<UserPreviewProjection> findByActiveTrueAndUsernameContainingIgnoreCaseOrActiveTrueAndNameContainingIgnoreCase(
            String username, String name, Pageable pageable) {

        return jpaUserRepository.findByActiveTrueAndUsernameContainingIgnoreCaseOrActiveTrueAndNameContainingIgnoreCase(username, name, pageable);
    }

    @Override
    public boolean existsByUsername(String username) {
        return jpaUserRepository.existsByUsernameIgnoreCase(username);
    }
}
