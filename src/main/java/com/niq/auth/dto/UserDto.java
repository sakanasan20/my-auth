package com.niq.auth.dto;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

	    private Long id;

	    private String username;

	    @JsonIgnore
	    private String password;
	    
	    @Builder.Default
	    private boolean accountNonExpired = true;
	    
	    @Builder.Default
	    private boolean accountNonLocked = true;
	    
	    @Builder.Default
	    private boolean credentialsNonExpired = true;
	    
	    @Builder.Default
	    private boolean enabled = true;
	    
	    @Builder.Default
	    private Set<RoleDto> roles = new HashSet<>();
	    
	    private String email;
	    
	    private Instant createdAt;

	    private String createdBy;

	    private Instant updatedAt;

	    private String updatedBy;

	    @Builder.Default
	    private List<LicenseDto> licenses = new ArrayList<>();
}
