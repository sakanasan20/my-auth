package com.niq.auth.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.niq.auth.dto.AuthorityDto;
import com.niq.auth.service.AuthorityService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/authorities")
public class AuthorityController {

	private final AuthorityService authorityService;
	
	@GetMapping
	public List<AuthorityDto> getAll() {
		return authorityService.getAll();
	}
	
}
