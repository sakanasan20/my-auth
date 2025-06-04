package com.niq.auth.bootstrap;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Set;
import java.util.UUID;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.niq.auth.entity.AppModule;
import com.niq.auth.entity.AppSystem;
import com.niq.auth.entity.License;
import com.niq.auth.entity.User;
import com.niq.auth.repository.AppModuleRepository;
import com.niq.auth.repository.AppSystemRepository;
import com.niq.auth.repository.LicenseRepository;
import com.niq.auth.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Order(14)
@Slf4j
@Component
@RequiredArgsConstructor
public class LicenseInitializer implements CommandLineRunner {

	private final UserRepository userRepository;
	private final AppSystemRepository appSystemRepository;
	private final AppModuleRepository appModuleRepository;
	private final LicenseRepository licenseRepository;

	@Transactional
	@Override
	public void run(String... args) {

		AppSystem systemScm = appSystemRepository.findByName("SCM").orElseThrow(RuntimeException::new);
		AppSystem systemCrm = appSystemRepository.findByName("CRM").orElseThrow(RuntimeException::new);

		User userScmAdmin = userRepository.findByUsername("scmadmin").orElseThrow(RuntimeException::new);
		User userCrmAdmin = userRepository.findByUsername("crmadmin").orElseThrow(RuntimeException::new);

		AppModule moduleSupplier = appModuleRepository.findByName("Supplier").orElseThrow(RuntimeException::new);
		AppModule moduleInventory = appModuleRepository.findByName("Inventory").orElseThrow(RuntimeException::new);
		AppModule moduleContact = appModuleRepository.findByName("Contact").orElseThrow(RuntimeException::new);
		AppModule moduleSales = appModuleRepository.findByName("Sales").orElseThrow(RuntimeException::new);

		licenseRepository.deleteAllInBatch();
		
		saveLicense(UUID.randomUUID().toString(), Instant.now(), Instant.now().plus(30, ChronoUnit.DAYS), userScmAdmin,
				Set.of(systemScm), Set.of(moduleSupplier, moduleInventory));
		saveLicense(UUID.randomUUID().toString(), Instant.now(), Instant.now().plus(30, ChronoUnit.DAYS), userCrmAdmin,
				Set.of(systemCrm), Set.of(moduleContact, moduleSales));
	}

	@Transactional(readOnly = true)
	private License saveLicense(String licenseKey, Instant issuedAt, Instant expiresAt, User user,
			Set<AppSystem> systems, Set<AppModule> modules) {
		
		License license = licenseRepository.saveAndFlush(License.builder()
				.licenseKey(licenseKey)
				.issuedAt(issuedAt)
				.expiresAt(expiresAt)
				.user(user)
				.systems(systems)
				.modules(modules)
				.build());
		log.info("授權憑證（授權碼：{}）已建立", license.getLicenseKey());
		return license;
	}
}
