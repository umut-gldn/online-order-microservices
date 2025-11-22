package com.onlineorder.userservice.service.impl;

import com.onlineorder.common.config.RabbitMQConfig;
import com.onlineorder.common.events.UserEvent;
import com.onlineorder.common.messaging.MessagePublisher;
import com.onlineorder.userservice.dto.CreateUserRequest;
import com.onlineorder.userservice.dto.UpdateUserRequest;
import com.onlineorder.userservice.dto.UserResponse;
import com.onlineorder.userservice.exception.ConflictException;
import com.onlineorder.userservice.exception.NotFoundException;
import com.onlineorder.userservice.mapper.UserMapper;
import com.onlineorder.userservice.model.User;
import com.onlineorder.userservice.repository.UserRepository;
import com.onlineorder.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final MessagePublisher messagePublisher;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserResponse create(CreateUserRequest request) {
        if(userRepository.existsByEmail(request.email())){
            throw new ConflictException("Email already in use: "+request.email());
        }
        User user=userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.password()));
        User saved=userRepository.save(user);

        UserEvent event=new UserEvent();
        event.setUserId(saved.getId());
        event.setEmail(saved.getEmail());
        event.setFirstName(saved.getFullName());
        event.setLastName(null);
        event.setPhoneNumber(null);
        event.setEventType(UserEvent.UserEventType.USER_CREATED);
        event.setEventTime(LocalDateTime.now());
        messagePublisher.publishMessage(RabbitMQConfig.USER_EXCHANGE,
                RabbitMQConfig.USER_CREATED_KEY,event);

        return userMapper.toResponse(saved);
    }

    @Override
    public Page<UserResponse> list(Pageable pageable) {
        return userRepository.findAll(pageable).map(userMapper::toResponse);
    }

    @Override
    public UserResponse getById(Long id) {
        User user=userRepository.findById(id).orElseThrow(()-> new NotFoundException("User not found: "+id));
        return userMapper.toResponse(user);
    }

    @Override
    public UserResponse update(Long id, UpdateUserRequest request) {
        User user=userRepository.findById(id).orElseThrow(()-> new NotFoundException("User not found: "+id));

        userMapper.updateUserFromDto(request,user);
        return userMapper.toResponse(userRepository.save(user));
    }

    @Override
    public void delete(Long id) {
        if(!userRepository.existsById(id)){
            throw new NotFoundException("User not found: "+id);
        }
        userRepository.deleteById(id);
    }
}
