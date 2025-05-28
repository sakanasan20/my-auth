package com.niq.auth.bootstrap;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.niq.auth.entity.AppSystem;
import com.niq.auth.repository.AppSystemRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Order(11)
@Slf4j
@Component
@RequiredArgsConstructor
public class SystemInitializer implements CommandLineRunner {

	private final AppSystemRepository appSystemRepository;

	@Transactional
	@Override
	public void run(String... args) {
		saveSystemIfNotExists("system:scm", "SCM", "Supply Chain Management");
		saveSystemIfNotExists("system:crm", "CRM", "Customer Relationship Management");
	}

	@Transactional(readOnly = true)
	private AppSystem saveSystemIfNotExists(String code, String name, String description) {

		AppSystem systemFound = appSystemRepository.findByName(name).orElseGet(() -> {
			AppSystem systemCreated = appSystemRepository
					.saveAndFlush(AppSystem.builder().code(code).name(name).description(description).build());
			log.info("系統（名稱：{}）已建立", systemCreated.getName());
			return systemCreated;
		});

		return systemFound;
	}
}
