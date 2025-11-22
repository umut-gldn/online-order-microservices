package com.onlineorder.userservice.service;

import com.onlineorder.userservice.model.User;
import com.onlineorder.userservice.repository.UserRepository;
import com.onlineorder.userservice.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user=userRepository.findByEmail(email).orElseThrow(()-> new UsernameNotFoundException("User not found: "+email));
        return new CustomUserDetails(user);
    }
}
