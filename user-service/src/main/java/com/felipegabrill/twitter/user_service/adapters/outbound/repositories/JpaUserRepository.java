package com.felipegabrill.twitter.user_service.adapters.outbound.repositories;

import com.felipegabrill.twitter.user_service.adapters.outbound.entities.JpaUserEntity;
import com.felipegabrill.twitter.user_service.domain.user.projections.UserPreviewProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface JpaUserRepository extends JpaRepository<JpaUserEntity, UUID> {

    Page<UserPreviewProjection>
    findByActiveTrueAndUsernameContainingIgnoreCaseOrActiveTrueAndNameContainingIgnoreCase(
            String username,
            String name,
            Pageable pageable
    );


    Optional<JpaUserEntity> findByUsernameAndActiveTrue(String Username);

    Optional<JpaUserEntity> findByIdAndActiveTrue(UUID id);

    boolean existsByUsernameIgnoreCase(String username);
}
