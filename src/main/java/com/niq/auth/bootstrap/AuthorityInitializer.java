package com.niq.auth.bootstrap;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.niq.auth.entity.Authority;
import com.niq.auth.repository.AuthorityRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Order(1)
@Slf4j
@Component
@RequiredArgsConstructor
public class AuthorityInitializer implements CommandLineRunner {

	private final AuthorityRepository authorityRepository;

	@Transactional
	@Override
	public void run(String... args) {
		saveAuthorityIfNotExists("READ", "讀取");
		saveAuthorityIfNotExists("WRITE", "寫入");
		saveAuthorityIfNotExists("DELETE", "刪除");
	}

	@Transactional(readOnly = true)
	private Authority saveAuthorityIfNotExists(String name, String description) {

		Authority authorityFound = authorityRepository.findByName(name).orElseGet(() -> {
			Authority authorityCreated = authorityRepository
					.saveAndFlush(Authority.builder().name(name).description(description).build());
			log.info("權限（名稱：{}）已建立", authorityCreated.getName());
			return authorityCreated;
		});

		return authorityFound;
	}

}
