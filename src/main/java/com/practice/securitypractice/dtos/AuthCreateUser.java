package com.practice.securitypractice.dtos;

import jakarta.validation.constraints.NotBlank;

public record AuthCreateUser(
    @NotBlank String username,
    @NotBlank String email, 
    @NotBlank String password, 
    @NotBlank String phone_number) {
}
