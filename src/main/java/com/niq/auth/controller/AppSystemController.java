package com.niq.auth.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.niq.auth.dto.AppSystemDto;
import com.niq.auth.service.AppSystemService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/systems")
public class AppSystemController {

	private final AppSystemService appSystemService;
	
	@GetMapping
	public List<AppSystemDto> getAll() {
		return appSystemService.getAll();
	} 
}
