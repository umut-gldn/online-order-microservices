package com.onlineorder.userservice.service;

import com.onlineorder.userservice.dto.CreateUserRequest;
import com.onlineorder.userservice.exception.ConflictException;
import com.onlineorder.userservice.model.User;
import com.onlineorder.userservice.repository.UserRepository;
import com.onlineorder.userservice.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//check this class and maybe directly use jwtutil
@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public String login(String email,String password) {
        Authentication authenticate=authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email,password));

        if(authenticate.isAuthenticated()){
            return jwtUtil.generateToken(email);
        }
        else  throw new RuntimeException("Authentication failed");
    }

    public void validateToken(String token){
        // gateway entegration
    }
}
