package com.niq.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.niq.auth.entity.Jwk;

public interface JwkRepository extends JpaRepository<Jwk, String> {

}
