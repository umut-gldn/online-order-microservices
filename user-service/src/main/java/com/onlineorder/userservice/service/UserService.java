package com.onlineorder.userservice.service;

import com.onlineorder.userservice.dto.CreateUserRequest;
import com.onlineorder.userservice.dto.UpdateUserRequest;
import com.onlineorder.userservice.dto.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    UserResponse create(CreateUserRequest request);
    Page<UserResponse> list(Pageable pageable);
    UserResponse getById(Long id);
    UserResponse update(Long id, UpdateUserRequest request);
    void delete(Long id);
    
}
