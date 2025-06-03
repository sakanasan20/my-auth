package com.niq.auth.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.niq.auth.dto.RoleDto;
import com.niq.auth.service.RoleService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/roles")
public class RoleController {

	private final RoleService roleService;
	
	@GetMapping
	public List<RoleDto> getAll() {
		return roleService.getAll();
	}
	
}
