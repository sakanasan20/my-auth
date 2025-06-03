package com.niq.auth.converter;

import org.springframework.stereotype.Component;

import com.niq.auth.dto.AppFeatureDto;
import com.niq.auth.entity.AppFeature;

@Component
public class FeatureConverter {

	public AppFeatureDto toDto(AppFeature entity) {
		return AppFeatureDto.builder()
				.id(entity.getId())
				.code(entity.getCode())
				.name(entity.getName())
				.description(entity.getDescription())
				.moduleId(entity.getModule() != null ? entity.getModule().getId() : null)
				.build();
	}
	
}
