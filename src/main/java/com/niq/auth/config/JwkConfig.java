package com.niq.auth.config;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.niq.auth.security.PersistentJwkService;

@Configuration
public class JwkConfig {

    @Autowired(required = false)
    private PersistentJwkService persistentJwkService;  // 只在 postgres profile 存在

    /**
     * JWT 的簽章金鑰來源，會自動用於 access token 的簽章與 OpenID metadata (/jwks)
     * @return
     */
    @Bean
    JWKSource<SecurityContext> jwkSource() {
        RSAKey rsaKey = (persistentJwkService != null)
                ? persistentJwkService.loadOrCreateRsaKey()
                : generateRsa(); // fallback to memory for H2 or dev

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
}
