package com.practice.securitypractice.dtos;

import java.util.List;

import org.springframework.validation.annotation.Validated;

@Validated
public record AuthCreateRole(List<String> roleListName, String roleName) {
}
