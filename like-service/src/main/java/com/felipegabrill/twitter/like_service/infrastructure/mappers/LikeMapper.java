package com.felipegabrill.twitter.like_service.infrastructure.mappers;


import com.felipegabrill.twitter.like_service.adapters.outbound.entities.JpaLikeEntity;
import com.felipegabrill.twitter.like_service.domain.like.Like;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LikeMapper {

    Like toDomain(JpaLikeEntity entity);

    JpaLikeEntity toEntity(Like domain);
}
