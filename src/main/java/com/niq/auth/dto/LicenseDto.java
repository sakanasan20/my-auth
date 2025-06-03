package com.niq.auth.dto;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LicenseDto {

    private Long id;

    private String licenseKey;
    
    private Instant issuedAt;
    
    private Instant expiresAt;

    private Long userId;

    @Builder.Default
    private Set<AppSystemDto> systems = new HashSet<>();

    @Builder.Default
    private Set<AppModuleDto> modules = new HashSet<>();
	
}
