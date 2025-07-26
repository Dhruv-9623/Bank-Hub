package com.bankhub.user.dto;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class UserUpdateDto {

    @Email(message = "Email should be valid")
    private String email;

    private String firstName;

    private String lastName;
}