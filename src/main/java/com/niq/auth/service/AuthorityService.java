package com.niq.auth.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.niq.auth.converter.AuthorityConverter;
import com.niq.auth.dto.AuthorityDto;
import com.niq.auth.repository.AuthorityRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthorityService {
	
	private final AuthorityRepository authorityRepository;
	private final AuthorityConverter authorityConverter;

	public List<AuthorityDto> getAll() {
		return authorityRepository.findAll().stream().map(authorityConverter::toDto).toList();
	}

}
