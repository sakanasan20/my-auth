package com.niq.auth.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.niq.auth.converter.UserConverter;
import com.niq.auth.dto.UserDto;
import com.niq.auth.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	
	private final UserRepository userRepository;
	private final UserConverter userConverter;


	public List<UserDto> getAll() {
		return userRepository.findAll().stream().map(userConverter::toDto).toList();
	}
	
}
