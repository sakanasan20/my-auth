package com.niq.auth.converter;

import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.niq.auth.dto.RoleDto;
import com.niq.auth.entity.Role;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RoleConverter {
	
	private final AuthorityConverter authorityConverter;

	public RoleDto toDto(Role entity) {
		return RoleDto.builder()
				.id(entity.getId())
				.name(entity.getName())
				.description(entity.getDescription())
				.authorities(entity.getAuthorities().stream().map(authorityConverter::toDto).collect(Collectors.toSet()))
				.createdAt(entity.getCreatedAt())
				.createdBy(entity.getCreatedBy())
				.updatedAt(entity.getUpdatedAt())
				.updatedBy(entity.getUpdatedBy())
				.build();
	}
	
}
