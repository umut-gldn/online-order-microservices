package com.onlineorder.userservice.mapper;

import com.onlineorder.userservice.dto.CreateUserRequest;
import com.onlineorder.userservice.dto.UpdateUserRequest;
import com.onlineorder.userservice.dto.UserResponse;
import com.onlineorder.userservice.model.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring",nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {
    @Mapping(target = "password",ignore = true)
    User toEntity(CreateUserRequest request);

    UserResponse toResponse(User user);

    void updateUserFromDto(UpdateUserRequest request, @MappingTarget User user);
}
