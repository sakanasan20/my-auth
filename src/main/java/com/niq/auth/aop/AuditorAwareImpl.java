package com.niq.auth.aop;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Component;

import com.niq.auth.security.CustomUserDetails;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component("auditorProvider")
public class AuditorAwareImpl implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth.getPrincipal().equals("anonymousUser")) {
        	log.debug("getCurrentAuditor: Returning system as auditor");
            return Optional.of("system");
        }

        // 若 principal 是你自訂的 UserDetails 實作
        Object principal = auth.getPrincipal();
        if (principal instanceof CustomUserDetails userDetails) {
        	log.debug("getCurrentAuditor: Returning user {}", userDetails.getUsername());
            return Optional.of(userDetails.getUsername());
        } else if (principal instanceof OidcUser) {
        	log.debug("getCurrentAuditor: Returning email {}", ((OidcUser) principal).getEmail());
            return Optional.of(((OidcUser) principal).getEmail()); // 或其他欄位
        }

        log.debug("getCurrentAuditor: Returning default {}", auth.getName());
        return Optional.of(auth.getName());
    }
}
