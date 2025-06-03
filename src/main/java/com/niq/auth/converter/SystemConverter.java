package com.niq.auth.converter;

import org.springframework.stereotype.Component;

import com.niq.auth.dto.AppSystemDto;
import com.niq.auth.entity.AppSystem;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SystemConverter {
	
	private final ModuleConverter moduleConverter;

	public AppSystemDto toDto(AppSystem entity) {
		return AppSystemDto.builder()
				.id(entity.getId())
				.code(entity.getCode())
				.name(entity.getName())
				.description(entity.getDescription())
				.modules(entity.getModules().stream().map(moduleConverter::toDto).toList())
				.build();
	}
	
}
