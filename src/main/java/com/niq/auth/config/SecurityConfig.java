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

	@Bean
	PasswordEncoder passwordEncoder() {
	    return PasswordEncoderFactories.createDelegatingPasswordEncoder(); // 支援 bcrypt 且向下相容
	}
	
	@Bean
	UserDetailsService userDetailsService() {
	    UserDetails user = User.withUsername("admin")
	        .password(passwordEncoder().encode("password"))
	        .roles("ADMIN") // 或可改成 ADMIN 等
	        .build();
	    return new InMemoryUserDetailsManager(user);
	}
	
	@Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
	        .csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**")) // h2
	        .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin())) // h2
            .authorizeHttpRequests(auth -> auth
        		.requestMatchers("/h2-console/**").permitAll() // h2
        		.requestMatchers("/oidc/logout").permitAll() // OIDC Logout
        		.anyRequest().authenticated()
        	)
            .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults())) // Resource Server
            .formLogin(Customizer.withDefaults()); // Form Login
        return http.build();
    }
}
