package com.niq.auth.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.niq.auth.converter.LicenseConverter;
import com.niq.auth.dto.LicenseDto;
import com.niq.auth.repository.LicenseRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LicenseService {

	private final LicenseRepository licenseRepository;
	private final LicenseConverter licenseConverter;

	public List<LicenseDto> getAll() {
		return licenseRepository.findAll().stream().map(licenseConverter::toDto).toList();
	}
	
}
