package com.niq.auth.converter;

import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;

import com.niq.auth.dto.UserInfoDto;
import com.niq.auth.entity.User;

@Component
public class UserConverter {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public UserInfoDto toUserInfoDto(User user) {
        return new UserInfoDto(
            user.getId(),
            user.getUsername(),
            user.getEmail(),
            user.isEnabled(),
            user.isAccountNonLocked(),
            user.isAccountNonExpired(),
            user.isCredentialsNonExpired(),
            user.getCreatedAt() != null ? user.getCreatedAt().format(FORMATTER) : null,
            user.getCreatedBy(),
            user.getUpdatedAt() != null ? user.getUpdatedAt().format(FORMATTER) : null,
            user.getUpdatedBy()
        );
    }
}
