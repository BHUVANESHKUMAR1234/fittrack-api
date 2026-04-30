package com.fittrack.mapper;

import com.fittrack.domain.entity.User;
import com.fittrack.dto.response.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import com.fittrack.dto.request.UpdateProfileRequest;
import org.mapstruct.BeanMapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponse toResponse(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromRequest(UpdateProfileRequest request, @MappingTarget User user);
}
