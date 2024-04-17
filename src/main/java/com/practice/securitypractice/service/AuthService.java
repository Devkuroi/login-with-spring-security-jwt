package com.practice.securitypractice.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.practice.securitypractice.dtos.AuthCreatePermission;
import com.practice.securitypractice.dtos.AuthCreateRole;
import com.practice.securitypractice.dtos.AuthCreateUser;
import com.practice.securitypractice.dtos.AuthLogin;
import com.practice.securitypractice.dtos.AuthResponse;
import com.practice.securitypractice.persistence.entities.PermissionEntity;
import com.practice.securitypractice.persistence.entities.RoleEntity;
import com.practice.securitypractice.persistence.entities.UserEntity;
import com.practice.securitypractice.persistence.repositories.PermissionRespository;
import com.practice.securitypractice.persistence.repositories.RoleRepository;
import com.practice.securitypractice.persistence.repositories.UserRepository;
import com.practice.securitypractice.utils.JwtUtils;

@Service
public class AuthService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository repository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PermissionRespository permissionRespository;

    @Autowired
    private JwtUtils jwtUtils;

    public AuthResponse createUser(AuthCreateUser authCreateUser) {

        Set<RoleEntity> roleList = repository.findRoleEntitiesByRoleNameIn(List.of( "ROLE_ADMIN"))
                .stream().collect(Collectors.toSet());
        

        if (roleList.isEmpty())
            throw new IllegalArgumentException("The roles specified does not exist.");

        UserEntity userEntity = UserEntity.builder()
                .username(authCreateUser.username())
                .email(authCreateUser.email())
                .password(passwordEncoder.encode(authCreateUser.password()))
                .phone_number(authCreateUser.phone_number())
                .roles(roleList)
                .build();

        Authentication authentication = new UsernamePasswordAuthenticationToken(userRepository.save(userEntity), null, roleList);

        String accessToken = jwtUtils.createJWT(authentication);

        AuthResponse authResponse = new AuthResponse("User created successfully",
                accessToken, true);
        return authResponse;
    }

    public AuthResponse loginUser(AuthLogin authLogin) {

        Authentication authentication = this.authenticate(authLogin.email(), authLogin.password());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = jwtUtils.createJWT(authentication);
        return new AuthResponse("User login succesfull", accessToken, true);

    }

    public Authentication authenticate(String email, String password) {
        UserDetails userDetails = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Email not Exist"));

        if (userDetails == null)
            throw new BadCredentialsException("Username or password is invalid");

        if (!passwordEncoder.matches(password, userDetails.getPassword()))
            throw new BadCredentialsException("Password is incorrect");

        return new UsernamePasswordAuthenticationToken(email, password, userDetails.getAuthorities());
    }

    public AuthResponse createRole(AuthCreateRole authCreateRole) {

        List<String> permission = authCreateRole.roleListName();

        Set<PermissionEntity> permissionList = permissionRespository.findPermissionEntitiesByNameIn(permission)
        .stream().collect(Collectors.toSet());

        Set<RoleEntity> roleList = repository.findRoleEntitiesByRoleNameIn(List.of(authCreateRole.roleName()))
                .stream().collect(Collectors.toSet());

        repository.findAll();

        if (!roleList.isEmpty()) 
            throw new IllegalArgumentException("The role already exists");

        
        if (permissionList.isEmpty() || permission.size() != permissionList.size()) 
            throw new IllegalArgumentException("The permission specified does not exist.");

        RoleEntity roleEntity = RoleEntity.builder()
        .roleName("ROLE_".concat(authCreateRole.roleName()))
        .permissionList(permissionList)
        .build();

        repository.save(roleEntity);

        return AuthResponse.builder().message("Role created succesfull").status(true).build();
    }

    public AuthResponse createPermission(AuthCreatePermission authCreatePermission) {

        if (permissionRespository.findByName(authCreatePermission.namePermission()) != null) 
            throw new IllegalArgumentException("The permission already exist.");

        PermissionEntity permissionEntity = PermissionEntity.builder().name(authCreatePermission.namePermission()).build();

        permissionRespository.save(permissionEntity);
        return AuthResponse.builder().build();
    }

}