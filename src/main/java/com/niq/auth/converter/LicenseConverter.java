package com.niq.auth.converter;

import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.niq.auth.dto.LicenseDto;
import com.niq.auth.entity.License;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class LicenseConverter {
	
	private final SystemConverter systemConverter;
	private final ModuleConverter moduleConverter;

	public LicenseDto toDto(License entity) {
		return LicenseDto.builder()
				.id(entity.getId())
				.licenseKey(entity.getLicenseKey())
				.issuedAt(entity.getIssuedAt())
				.expiresAt(entity.getExpiresAt())
				.userId(entity.getUser() != null ? entity.getUser().getId() : null)
				.systems(entity.getSystems().stream().map(systemConverter::toDto).collect(Collectors.toSet()))
				.modules(entity.getModules().stream().map(moduleConverter::toDto).collect(Collectors.toSet()))
				.build();
	}
	
}
