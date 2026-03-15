package com.bankhub.user.controller;

import com.bankhub.user.dto.*;
import com.bankhub.user.entity.UserRole;
import com.bankhub.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void registerUser_Success() throws Exception {
        UserRegistrationDto dto = new UserRegistrationDto();
        dto.setUsername("testuser");
        dto.setEmail("test@example.com");
        dto.setPassword("password123");
        dto.setFirstName("Test");
        dto.setLastName("User");

        UserResponseDto response = UserResponseDto.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .role(UserRole.CUSTOMER)
                .isActive(true)
                .build();

        when(userService.registerUser(any())).thenReturn(response);

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    void login_Success() throws Exception {
        LoginRequestDto dto = new LoginRequestDto();
        dto.setUsername("testuser");
        dto.setPassword("password123");

        UserResponseDto userResponse = UserResponseDto.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .role(UserRole.CUSTOMER)
                .build();

        AuthenticationResponseDto response = AuthenticationResponseDto.builder()
                .accessToken("accessToken")
                .refreshToken("refreshToken")
                .tokenType("Bearer")
                .expiresIn(3600L)
                .user(userResponse)
                .build();

        when(userService.authenticateUser(any())).thenReturn(response);

        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("accessToken"))
                .andExpect(jsonPath("$.tokenType").value("Bearer"));
    }

    @Test
    void getProfile_Success() throws Exception {
        UserResponseDto response = UserResponseDto.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .role(UserRole.CUSTOMER)
                .isActive(true)
                .build();

        when(userService.getUserProfile(any())).thenReturn(response);

        mockMvc.perform(get("/api/users/profile")
                        .header("Authorization", "Bearer testToken"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"));
    }
}