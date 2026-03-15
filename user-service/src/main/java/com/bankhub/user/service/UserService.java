package com.bankhub.user.service;

import com.bankhub.user.dto.*;
import com.bankhub.user.entity.User;
import com.bankhub.user.entity.UserRole;
import com.bankhub.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    // ...existing code...

    public UserResponseDto registerUser(UserRegistrationDto registrationDto) {
        // Check if user already exists
        if (userRepository.existsByUsernameOrEmail(
                registrationDto.getUsername(), registrationDto.getEmail())) {
            throw new RuntimeException("User with this username or email already exists");
        }

        // Create new user
        User user = User.builder()
                .username(registrationDto.getUsername())
                .email(registrationDto.getEmail())
                .passwordHash(passwordEncoder.encode(registrationDto.getPassword()))
                .role(UserRole.CUSTOMER)
                .isActive(true)
                .emailVerified(false)
                .build();

        User savedUser = userRepository.save(user);

        System.out.println("User registered successfully: " + savedUser.getUsername());

        return mapToUserResponseDto(savedUser);
    }

    public AuthenticationResponseDto authenticateUser(LoginRequestDto loginRequest) {
        User user = userRepository.findByUsernameOrEmail(
                        loginRequest.getUsername(), loginRequest.getUsername())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!user.getIsActive()) {
            throw new RuntimeException("User account is deactivated");
        }

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPasswordHash())) {
            throw new RuntimeException("Invalid credentials");
        }

        // Update last login
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);

        // Generate JWT tokens
        String accessToken = jwtTokenProvider.generateAccessToken(user);
        String refreshToken = jwtTokenProvider.generateRefreshToken(user);

        System.out.println("User authenticated successfully: " + user.getUsername());

        return AuthenticationResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(jwtTokenProvider.getAccessTokenValidityInSeconds())
                .user(mapToUserResponseDto(user))
                .build();
    }

    public UserResponseDto getUserProfile(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return mapToUserResponseDto(user);
    }

    public UserResponseDto updateUserProfile(String username, UserUpdateDto updateDto) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (updateDto.getEmail() != null) {
            user.setEmail(updateDto.getEmail());
        }

        User savedUser = userRepository.save(user);
        return mapToUserResponseDto(savedUser);
    }

    public void logoutUser(String username) {
        System.out.println("User logged out: " + username);
        // In a real application, you might invalidate tokens here
    }

    private UserResponseDto mapToUserResponseDto(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .isActive(user.getIsActive())
                .emailVerified(user.getEmailVerified())
                .createdAt(user.getCreatedAt())
                .lastLogin(user.getLastLogin())
                .build();
    }
}