package com.fittrack.service;

import com.fittrack.domain.entity.User;
import com.fittrack.dto.request.UpdateProfileRequest;
import com.fittrack.dto.response.UserResponse;
import com.fittrack.exception.ResourceNotFoundException;
import com.fittrack.mapper.UserMapper;
import com.fittrack.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Handles user profile operations.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional(readOnly = true)
    public UserResponse getProfile(String email) {
        log.debug("Fetching profile for: {}", email);
        User user = findByEmail(email);
        return userMapper.toResponse(user);
    }

    @Transactional
    public UserResponse updateProfile(String email, UpdateProfileRequest request) {
        log.info("Updating profile for: {}", email);
        User user = findByEmail(email);
        userMapper.updateFromRequest(request, user);
        User saved = userRepository.save(user);
        log.info("Profile updated for user: {}", saved.getId());
        return userMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + email));
    }

    @Transactional(readOnly = true)
    public User findById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + id));
    }
}
