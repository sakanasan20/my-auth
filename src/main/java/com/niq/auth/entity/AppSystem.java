package com.niq.auth.entity;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
@Table(name = "app_systems")
public class AppSystem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 簡短代碼，例如 ERP, CRM, SCM
    @Column(unique = true, nullable = false)
    private String code;

    // 顯示名稱，例如「企業資源規劃系統」
    private String name;

    private String description;

    @OneToMany(mappedBy = "system", fetch = FetchType.EAGER)
    private List<AppModule> modules;
}
