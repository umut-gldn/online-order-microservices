package com.onlineorder.userservice.controller;

import com.onlineorder.userservice.dto.AuthRequest;
import com.onlineorder.userservice.dto.CreateUserRequest;
import com.onlineorder.userservice.dto.UserResponse;
import com.onlineorder.userservice.service.AuthService;
import com.onlineorder.userservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody @Valid CreateUserRequest request){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.create(request));
    }

    @PostMapping("/token")
    public ResponseEntity<String> getToken(@RequestBody @Valid AuthRequest request){
        String token=authService.login(request.email(), request.password());
        return ResponseEntity.ok(token);
    }
}
