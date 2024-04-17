package com.practice.securitypractice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.practice.securitypractice.dtos.AuthCreatePermission;
import com.practice.securitypractice.dtos.AuthCreateRole;
import com.practice.securitypractice.dtos.AuthCreateUser;
import com.practice.securitypractice.dtos.AuthLogin;
import com.practice.securitypractice.dtos.AuthResponse;
import com.practice.securitypractice.service.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

  @Autowired
  AuthService authService;

  @PostMapping("/register")
  public ResponseEntity<AuthResponse> register(@RequestBody @Valid AuthCreateUser authCreateUser) {
    return new ResponseEntity<>(this.authService.createUser(authCreateUser), HttpStatus.CREATED);
  }

  @PostMapping("/login")
  public ResponseEntity<AuthResponse> login(@RequestBody @Valid AuthLogin authLogin) {
    return new ResponseEntity<>(this.authService.loginUser(authLogin), HttpStatus.OK);
  }

  @PostMapping("/create-role")
  public ResponseEntity<AuthResponse> createRole(@RequestBody @Valid AuthCreateRole authCreateRole) {
    return new ResponseEntity<>(this.authService.createRole(authCreateRole), HttpStatus.CREATED);
  }

  @PostMapping("/create-permission")
  public ResponseEntity<AuthResponse> createPermission(@RequestBody @Valid AuthCreatePermission authCreatePermission) {
    return new ResponseEntity<>(this.authService.createPermission(authCreatePermission), HttpStatus.CREATED);
  }
}
