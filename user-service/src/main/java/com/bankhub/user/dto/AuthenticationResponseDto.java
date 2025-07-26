package com.bankhub.user.dto;

import lombok.Data;
import lombok.Builder;

@Data
@Builder
public class AuthenticationResponseDto {
    private String accessToken;
    private String refreshToken;
    private String tokenType;
    private Long expiresIn;
    private UserResponseDto user;
}