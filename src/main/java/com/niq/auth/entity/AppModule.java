package com.niq.auth.entity;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "app_modules")
public class AppModule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;       // e.g. "PURCHASE", "INVENTORY"
    private String name;       // e.g. "採購模組"
    private String description;

    @ManyToOne
    @JoinColumn(name = "system_id")
    private AppSystem system;

    @OneToMany(mappedBy = "module", fetch = FetchType.EAGER)
    private List<AppFeature> features;
}
