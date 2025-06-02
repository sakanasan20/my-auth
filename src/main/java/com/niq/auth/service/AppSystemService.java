package com.niq.auth.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.niq.auth.converter.AppSystemConverter;
import com.niq.auth.dto.AppSystemDto;
import com.niq.auth.repository.AppSystemRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AppSystemService {

	private final AppSystemRepository appSystemRepository;
	private final AppSystemConverter appSystemConverter;

	public List<AppSystemDto> getAll() {
		return appSystemRepository.findAll().stream().map(appSystemConverter::toDto).toList();
	}
	
}
