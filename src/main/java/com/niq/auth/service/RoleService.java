package com.niq.auth.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.niq.auth.converter.RoleConverter;
import com.niq.auth.dto.RoleDto;
import com.niq.auth.repository.RoleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoleService {
	
	private final RoleRepository roleRepository;
	private final RoleConverter roleConverter;

	public List<RoleDto> getAll() {
		return roleRepository.findAll().stream().map(roleConverter::toDto).toList();
	}

}
