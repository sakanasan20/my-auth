package com.niq.auth.entity;

import java.time.Instant;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
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
@Table(name = "licenses")
public class License {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String licenseKey;
    private Instant issuedAt;
    private Instant expiresAt;

    @ManyToOne
    private User user;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "license_systems",
        joinColumns = @JoinColumn(name = "license_id"),
        inverseJoinColumns = @JoinColumn(name = "system_id")
    )
    private Set<AppSystem> systems;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "license_modules",
        joinColumns = @JoinColumn(name = "license_id"),
        inverseJoinColumns = @JoinColumn(name = "module_id")
    )
    private Set<AppModule> modules;
}
