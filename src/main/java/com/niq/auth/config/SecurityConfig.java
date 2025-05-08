package com.niq.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

	/**
	 * 支援加密密碼
	 * @return
	 */
	@Bean
	PasswordEncoder passwordEncoder() {
	    // 支援 bcrypt 且向下相容
	    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}
	
	@Bean
	UserDetailsService userDetailsService() {
	    UserDetails user = User.withUsername("admin")
	        .password(passwordEncoder().encode("password")) // {noop} 表示不加密（僅限測試用）
	        .roles("ADMIN") // 或可改成 ADMIN 等
	        .build();
	    return new InMemoryUserDetailsManager(user);
	}
	
	@Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
	        .csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**"))
	        .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin()))
            .authorizeHttpRequests(auth -> auth
        		.requestMatchers("/h2-console/**").permitAll()
        		.requestMatchers("/oidc/logout").permitAll()
        		.anyRequest().authenticated()
        	)
            .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
            .formLogin(Customizer.withDefaults());
        return http.build();
    }
}
