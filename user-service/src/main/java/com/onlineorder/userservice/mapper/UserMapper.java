package com.onlineorder.userservice.mapper;

import com.onlineorder.userservice.dto.CreateUserRequest;
import com.onlineorder.userservice.dto.UpdateUserRequest;
import com.onlineorder.userservice.dto.UserResponse;
import com.onlineorder.userservice.model.User;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toEntity(CreateUserRequest request);

    UserResponse toResponse(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromRequest(UpdateUserRequest request, @MappingTarget User target);
}
