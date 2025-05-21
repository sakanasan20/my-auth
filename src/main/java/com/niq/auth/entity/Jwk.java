package com.niq.auth.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "jwk_keys")
public class Jwk {

    @Id
    private String keyId;

    @Lob
    private String publicKey;   // PEM 格式（或 Base64）

    @Lob
    private String privateKey;  // PEM 格式（或 Base64）
	
}
