package com.niq.auth.bootstrap;

import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.niq.auth.entity.Authority;
import com.niq.auth.entity.Role;
import com.niq.auth.repository.AuthorityRepository;
import com.niq.auth.repository.RoleRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Order(2)
@Slf4j
@Component
@RequiredArgsConstructor
public class RoleInitializer implements CommandLineRunner {

	private final RoleRepository roleRepository;
	private final AuthorityRepository authorityRepository;

	@Transactional
	@Override
	public void run(String... args) {

		Authority authorityRead = authorityRepository.findByName("READ").orElseThrow(RuntimeException::new);
		Authority authorityWrite = authorityRepository.findByName("WRITE").orElseThrow(RuntimeException::new);
		Authority authorityDelete = authorityRepository.findByName("DELETE").orElseThrow(RuntimeException::new);

		saveRoleIfNotExists("USER", "一般使用者", Set.of(authorityRead));
		saveRoleIfNotExists("ADMIN", "管理者", Set.of(authorityRead, authorityWrite, authorityDelete));
	}

	@Transactional(readOnly = true)
	private Role saveRoleIfNotExists(String name, String description, Set<Authority> authorities) {

		Role roleFound = roleRepository.findByName(name).orElseGet(() -> {
			Role roleCreated = roleRepository
					.save(Role.builder().name(name).description(description).authorities(authorities).build());
			log.info("角色（名稱：{}）已建立", roleCreated.getName());
			return roleCreated;
		});

		return roleFound;
	}

}
