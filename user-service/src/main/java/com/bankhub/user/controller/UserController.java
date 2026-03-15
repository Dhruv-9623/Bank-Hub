package com.bankhub.user.controller;

import com.bankhub.user.dto.*;
import com.bankhub.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@Validated
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UserResponseDto> register(
            @Valid @RequestBody UserRegistrationDto registrationDto) {

        System.out.println("User registration attempt for email: " + registrationDto.getEmail());

        UserResponseDto user = userService.registerUser(registrationDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponseDto> login(
            @Valid @RequestBody LoginRequestDto loginRequest) {

        System.out.println("Login attempt for username: " + loginRequest.getUsername());

        AuthenticationResponseDto response = userService.authenticateUser(loginRequest);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/profile")
    public ResponseEntity<UserResponseDto> getProfile(Authentication auth) {
        String username = auth.getName();
        UserResponseDto user = userService.getUserProfile(username);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/profile")
    public ResponseEntity<UserResponseDto> updateProfile(
            @Valid @RequestBody UserUpdateDto updateDto,
            Authentication auth) {

        String username = auth.getName();
        UserResponseDto updatedUser = userService.updateUserProfile(username, updateDto);
        return ResponseEntity.ok(updatedUser);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(Authentication auth) {
        userService.logoutUser(auth.getName());
        return ResponseEntity.ok("Logged out successfully");
    }

    // Test endpoint to verify service is working
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("User Service is working! 🎉");
    }
}