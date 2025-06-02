package com.niq.auth.converter;

import org.springframework.stereotype.Component;

import com.niq.auth.dto.AppModuleDto;
import com.niq.auth.entity.AppModule;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AppModuleConverter {
	
	private final AppFeatureConverter appFeatureConverter;
	
	public AppModuleDto toDto(AppModule entity) {
		return AppModuleDto.builder()
				.id(entity.getId())
				.code(entity.getCode())
				.name(entity.getName())
				.description(entity.getDescription())
				.features(entity.getFeatures().stream().map(appFeatureConverter::toDto).toList())
				.parentCode(entity.getSystem().getCode())
				.build();
	}

}
