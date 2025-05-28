package com.niq.auth.bootstrap;

import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.niq.auth.entity.Role;
import com.niq.auth.entity.User;
import com.niq.auth.repository.RoleRepository;
import com.niq.auth.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Order(3)
@Slf4j
@Component
@RequiredArgsConstructor
public class UserInitializer implements CommandLineRunner {

	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final PasswordEncoder passwordEncoder;

	@Transactional
	@Override
	public void run(String... args) {

		Role roleUser = roleRepository.findByName("USER").orElseThrow(RuntimeException::new);
		Role roleAdmin = roleRepository.findByName("ADMIN").orElseThrow(RuntimeException::new);

		saveUserIfNotExists("admin", "admin", Set.of(roleAdmin), "admin@example.com");
		saveUserIfNotExists("scmadmin", "scmadmin", Set.of(roleAdmin), "scmadmin@example.com");
		saveUserIfNotExists("crmadmin", "crmadmin", Set.of(roleAdmin), "crmadmin@example.com");
		saveUserIfNotExists("user", "user", Set.of(roleUser), "user@example.com");

	}

	@Transactional(readOnly = true)
	private User saveUserIfNotExists(String username, String password, Set<Role> roles, String email) {

		User userFound = userRepository.findByUsername(username).orElseGet(() -> {
			User userCreated = userRepository.save(User.builder().username(username)
					.password(passwordEncoder.encode(password)).roles(roles).email(email).build());
			log.info("使用者（帳號：{}）已建立", userCreated.getUsername());
			return userCreated;
		});

		return userFound;
	}
}
