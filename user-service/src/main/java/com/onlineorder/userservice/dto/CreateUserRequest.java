package com.onlineorder.userservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


public record CreateUserRequest (
        @Email @NotBlank String email,
        @NotBlank @Size(min = 6,max = 64) String password,
        @NotBlank @Size(min = 2,max = 120)String fullName
){}


