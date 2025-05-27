package com.niq.auth.dto;

import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoDto {
    private Long id;
    private String username;
    private String email;
    private boolean enabled;
    private boolean accountNonLocked;
    private boolean accountNonExpired;
    private boolean credentialsNonExpired;
    private String createdAt;
    private String createdBy;
    private String updatedAt;
    private String updatedBy;
    
    public Map<String, Object> toClaims() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("username", username);
        map.put("email", email);
        map.put("enabled", enabled);
        map.put("accountNonLocked", accountNonLocked);
        map.put("accountNonExpired", accountNonExpired);
        map.put("credentialsNonExpired", credentialsNonExpired);
        map.put("createdAt", createdAt);
        map.put("createdBy", createdBy);
        map.put("updatedAt", updatedAt);
        map.put("updatedBy", updatedBy);
        return map;
    }
}
