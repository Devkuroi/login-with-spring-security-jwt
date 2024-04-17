package com.practice.securitypractice;

import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.practice.securitypractice.persistence.entities.PermissionEntity;
import com.practice.securitypractice.persistence.entities.RoleEntity;
import com.practice.securitypractice.persistence.entities.UserEntity;
import com.practice.securitypractice.persistence.repositories.UserRepository;

@SpringBootApplication
public class SecurityPracticeApplication {

	public static void main(String[] args) {
		SpringApplication.run(SecurityPracticeApplication.class, args);
	}

	@Bean
	CommandLineRunner init(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		return args -> {
			PermissionEntity readPermission = PermissionEntity.builder()
					.name("READ")
					.build();

			PermissionEntity writePermission = PermissionEntity.builder()
					.name("WRITE")
					.build();

			RoleEntity roleUser = RoleEntity.builder()
					.roleName("ROLE_USER")
					.permissionList(Set.of(readPermission))
					.build();

			RoleEntity roleAdmin = RoleEntity.builder()
					.roleName("ROLE_ADMIN")
					.permissionList(Set.of(readPermission, writePermission))
					.build();
			
			UserEntity admin = UserEntity.builder()
					.username("admin")
					.email("e@gmail.com")
					.password(passwordEncoder.encode("1234"))
					.phone_number("1231231234")
					.roles(Set.of(roleAdmin, roleUser))
					.build();

			userRepository.save(admin);
		};
	}
}
