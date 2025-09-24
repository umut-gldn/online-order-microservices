package com.onlineorder.userservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UpdateUserRequest (
     @Email String email,
     @Size(min = 6,max = 64) String password,
     @Size(min = 2,max = 120) String fullName
){}
