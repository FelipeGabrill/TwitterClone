package com.felipegabrill.twitter.follow_service.mapper;

import com.felipegabrill.twitter.follow_service.database.model.Follow;
import com.felipegabrill.twitter.follow_service.dtos.follow.FollowResponseDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FollowMapper {

    FollowResponseDTO toDTO(Follow follow);

    Follow toEntity(FollowResponseDTO dto);

}
