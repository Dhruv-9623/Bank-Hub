package com.bankhub.user.service;

import com.bankhub.user.dto.*;
import com.bankhub.user.entity.User;
import com.bankhub.user.entity.UserRole;
import com.bankhub.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private UserService userService;

    private UserRegistrationDto registrationDto;
    private LoginRequestDto loginRequest;
    private User testUser;

    @BeforeEach
    void setUp() {
        registrationDto = new UserRegistrationDto();
        registrationDto.setUsername("testuser");
        registrationDto.setEmail("test@example.com");
        registrationDto.setPassword("password123");
        registrationDto.setFirstName("Test");
        registrationDto.setLastName("User");

        loginRequest = new LoginRequestDto();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("password123");

        testUser = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .passwordHash("encodedPassword")
                .role(UserRole.CUSTOMER)
                .isActive(true)
                .emailVerified(false)
                .build();
    }

    @Test
    void registerUser_Success() {
        when(userRepository.existsByUsernameOrEmail(any(), any())).thenReturn(false);
        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        UserResponseDto result = userService.registerUser(registrationDto);

        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        assertEquals("test@example.com", result.getEmail());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void registerUser_DuplicateUser_ThrowsException() {
        when(userRepository.existsByUsernameOrEmail(any(), any())).thenReturn(true);

        assertThrows(RuntimeException.class, () -> userService.registerUser(registrationDto));
        verify(userRepository, never()).save(any());
    }

    @Test
    void authenticateUser_Success() {
        when(userRepository.findByUsernameOrEmail(any(), any())).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(any(), any())).thenReturn(true);
        when(jwtTokenProvider.generateAccessToken(any())).thenReturn("accessToken");
        when(jwtTokenProvider.generateRefreshToken(any())).thenReturn("refreshToken");
        when(jwtTokenProvider.getAccessTokenValidityInSeconds()).thenReturn(3600L);

        AuthenticationResponseDto result = userService.authenticateUser(loginRequest);

        assertNotNull(result);
        assertEquals("accessToken", result.getAccessToken());
        assertEquals("refreshToken", result.getRefreshToken());
        assertEquals("Bearer", result.getTokenType());
    }

    @Test
    void authenticateUser_InvalidCredentials_ThrowsException() {
        when(userRepository.findByUsernameOrEmail(any(), any())).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(any(), any())).thenReturn(false);

        assertThrows(RuntimeException.class, () -> userService.authenticateUser(loginRequest));
    }

    @Test
    void getUserProfile_Success() {
        when(userRepository.findByUsername(any())).thenReturn(Optional.of(testUser));

        UserResponseDto result = userService.getUserProfile("testuser");

        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        assertEquals(UserRole.CUSTOMER, result.getRole());
    }

    @Test
    void getUserProfile_UserNotFound_ThrowsException() {
        when(userRepository.findByUsername(any())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.getUserProfile("nonexistent"));
    }
}