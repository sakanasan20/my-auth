package com.niq.auth.bootstrap;

import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.niq.auth.entity.Authority;
import com.niq.auth.entity.Role;
import com.niq.auth.entity.User;
import com.niq.auth.repository.AuthorityRepository;
import com.niq.auth.repository.RoleRepository;
import com.niq.auth.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

	private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder passwordEncoder;

	@Transactional
    @Override
    public void run(String... args) {
    	
    	// 1. 建立權限
        Authority authorityRead = saveAuthorityIfNotExists("READ", "讀取");
        Authority authorityWrite = saveAuthorityIfNotExists("WRITE", "寫入");
        Authority authorityDelete = saveAuthorityIfNotExists("DELETE", "刪除");

        // 2. 建立角色並關聯權限
        Role roleUser = saveRoleIfNotExists("USER", "一般使用者", Set.of(authorityRead));
        Role roleAdmin = saveRoleIfNotExists("ADMIN", "管理者", Set.of(authorityRead, authorityWrite, authorityDelete));

        // 3. 建立 admin 使用者
        String adminUsername = "admin";
        if (userRepository.findByUsername(adminUsername).isEmpty()) {
            User admin = User.builder()
                    .username(adminUsername)
                    .password(passwordEncoder.encode("admin"))
                    .roles(Set.of(roleAdmin))
                    .email("admin@example.com")
                    .build();
            userRepository.save(admin);
            userRepository.flush();
            log.info("建立 admin 使用者（帳號：{}）", adminUsername);
        } else {
            log.info("admin 使用者已存在，略過建立");
        }

        // 4. 建立一般使用者
        String userUsername = "user";
        if (userRepository.findByUsername(userUsername).isEmpty()) {
            User user = User.builder()
                    .username(userUsername)
                    .password(passwordEncoder.encode("user"))
                    .roles(Set.of(roleUser))
                    .email("user@example.com")
                    .build();
            userRepository.save(user);
            userRepository.flush();
            log.info("建立一般使用者（帳號：{}）", userUsername);
        } else {
            log.info("一般使用者已存在，略過建立");
        }
    }
	
	@Transactional(readOnly = true)
    private Authority saveAuthorityIfNotExists(String name, String description) {
        return authorityRepository.findByName(name)
                .orElseGet(() -> {
                	Authority authority = Authority.builder()
                			.name(name)
                			.description(description)
                			.build();
                	return authorityRepository.save(authority);
                });
    }

    @Transactional(readOnly = true)
    private Role saveRoleIfNotExists(String name, String description, Set<Authority> authorities) {
        return roleRepository.findByName(name)
                .orElseGet(() -> {
                    Role role = Role.builder()
                    		.name(name)
                    		.description(description)
                    		.authorities(authorities)
                    		.build();
                    return roleRepository.save(role);
                });
    }
}
