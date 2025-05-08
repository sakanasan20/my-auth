package com.niq.auth.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "`authorizationConsent`")
@IdClass(AuthorizationConsent.AuthorizationConsentId.class)
public class AuthorizationConsent {
	
	@Id
	private String registeredClientId;
	
	@Id
	private String principalName;
	
	@Column(length = 1000)
	private String authorities;

	@Data
	public static class AuthorizationConsentId implements Serializable {

		private static final long serialVersionUID = -4464609541573230131L;

		private String registeredClientId;
		
		private String principalName;
	}
}
