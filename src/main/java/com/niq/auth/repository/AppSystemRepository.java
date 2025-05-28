package com.niq.auth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.niq.auth.entity.AppSystem;

@Repository
public interface AppSystemRepository extends JpaRepository<AppSystem, Long> {

    Optional<AppSystem> findByName(String name);
}
