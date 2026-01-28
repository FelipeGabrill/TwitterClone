package com.felipegabrill.twitter.user_service.adapters.outbound.repositories;

import com.felipegabrill.twitter.user_service.adapters.outbound.entities.JpaUserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface JpaUserRepository extends JpaRepository<JpaUserEntity, UUID> {

    Optional<JpaUserEntity> findByUsername(String username);

    Page<JpaUserEntity> findByUsernameContainingIgnoreCaseOrNameContainingIgnoreCase(
            String username, String name, Pageable pageable);

}
