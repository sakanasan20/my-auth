package com.niq.auth.security;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.RSAKey;
import com.niq.auth.entity.Jwk;
import com.niq.auth.repository.JwkRepository;

import lombok.RequiredArgsConstructor;

@Service
@Profile({ "dev", "prod" })
@RequiredArgsConstructor
public class PersistentJwkService {

	private final JwkRepository jwkRepository;
	
    public RSAKey loadOrCreateRsaKey() {
        return jwkRepository.findById("auth-key")
            .map(this::convertToRSAKey)
            .orElseGet(() -> {
                RSAKey newKey = generateRsa();
                saveKeyToDb(newKey);
                return newKey;
            });
    }

    private void saveKeyToDb(RSAKey rsaKey) {
		try {
			Jwk jwk = Jwk.builder()
					.keyId("auth-key")
					.publicKey(Base64.getEncoder().encodeToString(rsaKey.toRSAPublicKey().getEncoded()))
					.privateKey(Base64.getEncoder().encodeToString(rsaKey.toRSAPrivateKey().getEncoded()))
					.build();
			jwkRepository.save(jwk);
		} catch (JOSEException e) {
			throw new IllegalStateException("轉換 RSAKey 時發生錯誤", e);
		}
    }

    private RSAKey convertToRSAKey(Jwk jwk) {
        try {
            byte[] publicBytes = Base64.getDecoder().decode(jwk.getPublicKey());
            byte[] privateBytes = Base64.getDecoder().decode(jwk.getPrivateKey());

            KeyFactory factory = KeyFactory.getInstance("RSA");
            RSAPublicKey pubKey = (RSAPublicKey) factory.generatePublic(new X509EncodedKeySpec(publicBytes));
            RSAPrivateKey privKey = (RSAPrivateKey) factory.generatePrivate(new PKCS8EncodedKeySpec(privateBytes));

            return new RSAKey.Builder(pubKey)
                    .privateKey(privKey)
                    .keyID(jwk.getKeyId())
                    .build();
        } catch (Exception e) {
            throw new IllegalStateException("載入 RSA 金鑰失敗", e);
        }
    }

    private RSAKey generateRsa() {
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(2048);
            KeyPair keyPair = generator.generateKeyPair();
            return new RSAKey.Builder((RSAPublicKey) keyPair.getPublic())
                    .privateKey(keyPair.getPrivate())
                    .keyID("auth-key")
                    .build();
        } catch (Exception e) {
            throw new IllegalStateException("產生 RSA 金鑰失敗", e);
        }
    }
}
