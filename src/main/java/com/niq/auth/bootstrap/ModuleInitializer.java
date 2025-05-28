package com.niq.auth.bootstrap;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.niq.auth.entity.AppModule;
import com.niq.auth.entity.AppSystem;
import com.niq.auth.repository.AppModuleRepository;
import com.niq.auth.repository.AppSystemRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Order(12)
@Slf4j
@Component
@RequiredArgsConstructor
public class ModuleInitializer implements CommandLineRunner {

	private final AppSystemRepository appSystemRepository;
	private final AppModuleRepository appModuleRepository;

	@Transactional
	@Override
	public void run(String... args) {

		AppSystem systemScm = appSystemRepository.findByName("SCM").orElseThrow(RuntimeException::new);
		AppSystem systemCrm = appSystemRepository.findByName("CRM").orElseThrow(RuntimeException::new);

		saveModuleIfNotExists("module:scm.inventory", "Inventory", "庫存管理", systemScm);
		saveModuleIfNotExists("module:scm.supplier", "Supplier", "供應商管理", systemScm);
		saveModuleIfNotExists("module:crm.contact", "Contact", "聯絡人管理", systemCrm);
		saveModuleIfNotExists("module:crm.sales", "Sales", "銷售管理", systemCrm);
	}

	@Transactional(readOnly = true)
	private AppModule saveModuleIfNotExists(String code, String name, String description, AppSystem system) {

		AppModule moduleFound = appModuleRepository.findByName(name).orElseGet(() -> {
			AppModule moduleCreated = appModuleRepository.saveAndFlush(
					AppModule.builder().code(code).name(name).description(description).system(system).build());
			log.info("模組（名稱：{}）已建立", moduleCreated.getName());
			return moduleCreated;
		});

		return moduleFound;
	}
}
