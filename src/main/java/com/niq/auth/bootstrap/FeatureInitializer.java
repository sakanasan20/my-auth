package com.niq.auth.bootstrap;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.niq.auth.entity.AppFeature;
import com.niq.auth.entity.AppModule;
import com.niq.auth.repository.AppFeatureRepository;
import com.niq.auth.repository.AppModuleRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Order(13)
@Slf4j
@Component
@RequiredArgsConstructor
public class FeatureInitializer implements CommandLineRunner {

	private final AppModuleRepository appModuleRepository;
	private final AppFeatureRepository appFeatureRepository;

	@Transactional
	@Override
	public void run(String... args) {

		AppModule moduleSupplier = appModuleRepository.findByName("Supplier").orElseThrow(RuntimeException::new);
		AppModule moduleInventory = appModuleRepository.findByName("Inventory").orElseThrow(RuntimeException::new);
		AppModule moduleContact = appModuleRepository.findByName("Contact").orElseThrow(RuntimeException::new);
		AppModule moduleSales = appModuleRepository.findByName("Sales").orElseThrow(RuntimeException::new);

		saveFeatureIfNotExists("feature:scm.supplier.create-purchase-order", "CreatePurchaseOrder", "建立採購單", moduleSupplier);
		saveFeatureIfNotExists("feature:scm.supplier.export-inventory-report", "ExportInventoryReport", "匯出庫存報表", moduleInventory);
		saveFeatureIfNotExists("feature:crm.contact.create-contact", "CreateContact", "建立聯絡人", moduleContact);
		saveFeatureIfNotExists("feature:crm.sales.view-sales-report", "ViewSalesReport", "檢視銷售報表", moduleSales);
	}

	@Transactional(readOnly = true)
	private AppFeature saveFeatureIfNotExists(String code, String name, String description, AppModule module) {

		AppFeature featureFound = appFeatureRepository.findByName(name).orElseGet(() -> {
			AppFeature featureCreated = appFeatureRepository.saveAndFlush(AppFeature.builder()
					.code(code)
					.name(name)
					.description(description)
					.module(module)
					.build());
			log.info("功能（名稱：{}）已建立", featureCreated.getName());
			return featureCreated;
		});

		return featureFound;
	}
}
