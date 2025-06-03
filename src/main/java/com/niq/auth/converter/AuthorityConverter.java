package com.niq.auth.converter;

import org.springframework.stereotype.Component;

import com.niq.auth.dto.AuthorityDto;
import com.niq.auth.entity.Authority;

@Component
public class AuthorityConverter {

	public AuthorityDto toDto(Authority entity) {
		return AuthorityDto.builder()
				.id(entity.getId())
				.name(entity.getName())
				.description(entity.getDescription())
				.createdAt(entity.getCreatedAt())
				.createdBy(entity.getCreatedBy())
				.updatedAt(entity.getUpdatedAt())
				.updatedBy(entity.getUpdatedBy())
				.build();
	}
	
}
