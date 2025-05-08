package com.niq.auth.config;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPublicKey;
import java.time.Duration;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
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

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.niq.auth.converter.UserConverter;
import com.niq.auth.dto.UserInfoDto;
import com.niq.auth.entity.User;
import com.niq.auth.security.CustomUserDetails;

@Configuration
public class AuthorizationServerConfig {
	
	@Autowired
	private UserConverter userConverter;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Bean
	OAuth2TokenCustomizer<JwtEncodingContext> tokenCustomizer() {
	    return context -> {
	        Authentication principal = context.getPrincipal();
	        if (principal.getPrincipal() instanceof CustomUserDetails userDetails) {
	            List<String> roles = userDetails.getAuthorities().stream()
	                .map(GrantedAuthority::getAuthority)
	                .toList();

	            User user = userDetails.getUser();
	            UserInfoDto userInfo = userConverter.toUserInfoDto(user);
//
	            if (context.getTokenType().getValue().equals("id_token")) {
	                context.getClaims().claim("roles", roles);
	                context.getClaims().claim("user", userInfo.toClaims()); // toClaims() 將 DTO 轉為 Map，保證能被 JSON 正確序列化進 token
	            }

	            if (context.getTokenType().getValue().equals("access_token")) {
	            	context.getClaims().claim("authorities", roles);
	                context.getClaims().claim("roles", roles);
	                context.getClaims().claim("user", userInfo.toClaims()); // toClaims() 將 DTO 轉為 Map，保證能被 JSON 正確序列化進 token
	            }
	        }
	    };
	}
    
    @Bean
    SecurityFilterChain authServerSecurityFilterChain(HttpSecurity http) throws Exception {
		var authorizationServerConfigurer = new OAuth2AuthorizationServerConfigurer();
        authorizationServerConfigurer.oidc(Customizer.withDefaults());

        // 自訂端點
        authorizationServerConfigurer
                .oidc(Customizer.withDefaults()); // 啟用 OpenID Connect 支援（如需要）

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
	RegisteredClientRepository registeredClientRepository() {
        RegisteredClient registeredClient = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId("portal-client")
                .clientSecret(passwordEncoder.encode("portal-secret"))
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                .redirectUri("http://localhost:8080/login/oauth2/code/portal-client") 
                .scope(OidcScopes.OPENID)
                .scope("read")
                .clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build())
                .tokenSettings(TokenSettings.builder() //指定 access token 和 refresh token 的有效時間與是否重複使用 refresh token
                    .accessTokenTimeToLive(Duration.ofMinutes(30))
                    .refreshTokenTimeToLive(Duration.ofHours(8))
                    .reuseRefreshTokens(false)
                    .build())
                .build();
		
		return new InMemoryRegisteredClientRepository(registeredClient); // 若要接資料庫，也可改成 JdbcRegisteredClientRepository
	}

    /**
     * JWT 的簽章金鑰來源，會自動用於 access token 的簽章與 OpenID metadata (/jwks)
     * @return
     */
    @Bean
    JWKSource<SecurityContext> jwkSource() {
        RSAKey rsaKey = generateRsa();
        JWKSet jwkSet = new JWKSet(rsaKey);
        return (jwkSelector, context) -> jwkSelector.select(jwkSet);
    }

    private RSAKey generateRsa() {
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(2048);
            KeyPair keyPair = generator.generateKeyPair();

            return new RSAKey.Builder((RSAPublicKey) keyPair.getPublic())
                .privateKey(keyPair.getPrivate())
                .keyID(UUID.randomUUID().toString())
                .build();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
    
    @Bean
    AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder()
            .issuer("http://localhost:8081") // OpenID Connect metadata (/.well-known/openid-configuration) 所需的發行者 URL（issuer）
            .build();
    }
}
