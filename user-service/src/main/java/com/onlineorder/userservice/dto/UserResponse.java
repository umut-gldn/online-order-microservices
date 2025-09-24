package com.onlineorder.userservice.dto;

public record UserResponse (
     Long id,
     String email,
     String fullName
){}
