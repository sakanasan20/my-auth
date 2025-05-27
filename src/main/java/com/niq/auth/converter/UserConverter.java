package com.niq.auth.converter;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;

import com.niq.auth.dto.UserInfoDto;
import com.niq.auth.entity.User;

@Component
public class UserConverter {
	
	private static final ZoneId ZONE_TAIPEI = ZoneId.of("Asia/Taipei");
	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_ZONED_DATE_TIME;

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
}
