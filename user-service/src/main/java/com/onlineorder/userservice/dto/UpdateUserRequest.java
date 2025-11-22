package com.onlineorder.userservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UpdateUserRequest (
    @Size(min = 2,max = 120) String fullName
){}
