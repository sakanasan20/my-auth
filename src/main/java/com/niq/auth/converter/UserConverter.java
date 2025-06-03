package com.niq.auth.converter;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.niq.auth.dto.UserDto;
import com.niq.auth.dto.UserInfoDto;
import com.niq.auth.entity.User;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserConverter {
	
	private static final ZoneId ZONE_TAIPEI = ZoneId.of("Asia/Taipei");
	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_ZONED_DATE_TIME;

	private final RoleConverter roleConverter;
	private final LicenseConverter licenseConverter;
	
    public UserInfoDto toUserInfoDto(User user) {    	
        return new UserInfoDto(
            user.getId(),
            user.getUsername(),
            user.getEmail(),
            user.isEnabled(),
            user.isAccountNonLocked(),
            user.isAccountNonExpired(),
            user.isCredentialsNonExpired(),
            user.getCreatedAt() != null ? user.getCreatedAt().atZone(ZONE_TAIPEI).format(FORMATTER) : null,
            user.getCreatedBy(),
            user.getUpdatedAt() != null ? user.getUpdatedAt().atZone(ZONE_TAIPEI).format(FORMATTER) : null,
            user.getUpdatedBy()
        );
    }
    
    public UserDto toDto(User entity) {
    	return UserDto.builder()
    			.id(entity.getId())
    			.username(entity.getUsername())
    			.password(entity.getPassword())
    			.accountNonExpired(entity.isAccountNonExpired())
    			.accountNonLocked(entity.isAccountNonLocked())
    			.credentialsNonExpired(entity.isCredentialsNonExpired())
    			.enabled(entity.isEnabled())
    			.roles(entity.getRoles().stream().map(roleConverter::toDto).collect(Collectors.toSet()))
    			.email(entity.getEmail())
    			.createdAt(entity.getCreatedAt())
    			.createdBy(entity.getCreatedBy())
    			.updatedAt(entity.getUpdatedAt())
    			.updatedBy(entity.getUpdatedBy())
    			.licenses(entity.getLicenses().stream().map(licenseConverter::toDto).toList())
    			.build();
    }
}
