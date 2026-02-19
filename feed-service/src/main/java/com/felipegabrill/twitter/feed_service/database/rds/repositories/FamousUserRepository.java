package com.felipegabrill.twitter.feed_service.database.rds.repositories;

import com.felipegabrill.twitter.feed_service.database.rds.entities.FamousUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FamousUserRepository extends JpaRepository<FamousUserEntity, UUID> {

    boolean existsByUserIdAndIsFamousTrue(UUID userId);

}
