package com.practice.securitypractice.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor 
public class AuthResponse {

    String message;
    String jwt;
    Boolean status;

}
