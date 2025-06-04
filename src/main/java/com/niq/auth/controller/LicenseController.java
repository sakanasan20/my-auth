package com.niq.auth.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.niq.auth.dto.LicenseDto;
import com.niq.auth.service.LicenseService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/licenses")
public class LicenseController {

	private final LicenseService licenseService;
	
	@GetMapping
	public List<LicenseDto> getAll() {
		return licenseService.getAll();
	} 
}
