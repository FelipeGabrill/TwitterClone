package com.felipegabrill.twitter.user_service.infrastructure.mappers;

import com.felipegabrill.twitter.user_service.adapters.outbound.entities.JpaUserEntity;
import com.felipegabrill.twitter.user_service.domain.user.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toDomain(JpaUserEntity entity);

    JpaUserEntity toEntity(User domain);

}
