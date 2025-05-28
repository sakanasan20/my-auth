package com.niq.auth.config;

import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import com.niq.auth.converter.UserConverter;
import com.niq.auth.dto.UserInfoDto;
import com.niq.auth.entity.License;
import com.niq.auth.entity.User;
import com.niq.auth.security.CustomUserDetails;

@Configuration
public class AuthorizationServerConfig {
	
    @Value("${oauth2.redirect-uri}")
    private String redirectUri;
    
    @Value("${oauth2.issuer-uri}")
    private String issuerUri;
	
	@Autowired
	private UserConverter userConverter;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Bean
	OAuth2TokenCustomizer<JwtEncodingContext> tokenCustomizer() {
	    return context -> {
	        Authentication principal = context.getPrincipal();
	        if (principal.getPrincipal() instanceof CustomUserDetails userDetails) {
	        	
	        	Set<String> roleSet = userDetails.getAuthorities().stream()
	                .map(GrantedAuthority::getAuthority)
	                .collect(Collectors.toSet());
	        	String roles = String.join(" ", roleSet);

	            User user = userDetails.getUser();
	            UserInfoDto userInfo = userConverter.toUserInfoDto(user);
	            
	            Set<String> scopeSet = new HashSet<>();
	            Set<String> sysSet = new HashSet<>();
	            Set<String> modSet = new HashSet<>();
	            Set<String> featSet = new HashSet<>();
	            for (License license : user.getLicenses()) {
	                license.getSystems().forEach(sys -> {
	                	scopeSet.add(sys.getCode());
	                	sysSet.add(sys.getCode());
	                });
	                license.getModules().forEach(mod -> {
	                    scopeSet.add(mod.getCode());
	                    modSet.add(mod.getCode());
	                    mod.getFeatures().forEach(feat -> {
	                    	scopeSet.add(feat.getCode());
	                    	featSet.add(feat.getCode());
	                    });
	                });
	            }
	            String scopes = String.join(" ", scopeSet);
	            String systems = String.join(" ", sysSet);
	            String modules = String.join(" ", modSet);
	            String features = String.join(" ", featSet);

	            if (context.getTokenType().getValue().equals("id_token")) {
	                context.getClaims().claim("roles", roles);
	                context.getClaims().claim("user", userInfo.toClaims()); // toClaims() 將 DTO 轉為 Map，保證能被 JSON 正確序列化進 token
	                context.getClaims().claim("systems", systems);
	                context.getClaims().claim("modules", modules);
	                context.getClaims().claim("features", features);
	            }

	            if (context.getTokenType().getValue().equals("access_token")) {
	            	context.getClaims().claim("authorities", roles);
	                context.getClaims().claim("roles", roles);
	                context.getClaims().claim("user", userInfo.toClaims()); // toClaims() 將 DTO 轉為 Map，保證能被 JSON 正確序列化進 token
	                context.getClaims().claim("scope", scopes);
	            }
	        }
	    };
	}
    
    @Bean
    SecurityFilterChain authServerSecurityFilterChain(HttpSecurity http) throws Exception {
		var authorizationServerConfigurer = new OAuth2AuthorizationServerConfigurer();
        authorizationServerConfigurer.oidc(Customizer.withDefaults()); // 啟用 OpenID Connect 支援（如需要）

        http
            .securityMatcher(authorizationServerConfigurer.getEndpointsMatcher())
            .authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated())
            .exceptionHandling(exceptions -> exceptions
				.authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login"))
			)
            .csrf(csrf -> csrf.ignoringRequestMatchers(authorizationServerConfigurer.getEndpointsMatcher()))
            .with(authorizationServerConfigurer, (config) -> {});

        return http.build();
    }
    
	@Bean
	RegisteredClientRepository registeredClientRepository(JdbcTemplate jdbcTemplate) {
		JdbcRegisteredClientRepository repository = new JdbcRegisteredClientRepository(jdbcTemplate);

        if (repository.findByClientId("portal-client") == null) {

            RegisteredClient registeredClient = RegisteredClient.withId(UUID.randomUUID().toString())
                    .clientId("portal-client")
                    .clientSecret(passwordEncoder.encode("portal-secret"))
                    .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                    .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                    .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                    .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                    .redirectUri(redirectUri) 
                    .scope(OidcScopes.OPENID)
                    .scope("read")
                    .clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build())
                    .tokenSettings(TokenSettings.builder() //指定 access token 和 refresh token 的有效時間與是否重複使用 refresh token
                        .accessTokenTimeToLive(Duration.ofMinutes(30))
                        .refreshTokenTimeToLive(Duration.ofHours(8))
                        .reuseRefreshTokens(false)
                        .build())
                    .build();
        	
            repository.save(registeredClient);
        }
        
        return repository;
	}
    
    @Bean
    AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder()
            .issuer(issuerUri) // OpenID Connect metadata (/.well-known/openid-configuration) 所需的發行者 URL（issuer）
            .build();
    }
}
