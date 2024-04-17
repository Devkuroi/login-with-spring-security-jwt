package com.practice.securitypractice.dtos;

import org.springframework.validation.annotation.Validated;

@Validated
public record AuthCreatePermission(String namePermission) {

}
