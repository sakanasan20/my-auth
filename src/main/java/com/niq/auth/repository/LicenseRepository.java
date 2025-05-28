package com.niq.auth.repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.niq.auth.entity.License;

@Repository
public interface LicenseRepository extends JpaRepository<License, Long> {

    Optional<License> findByLicenseKey(String licenseKey);

    List<License> findByUser_Id(Long userId);

    List<License> findByExpiresAtBefore(Instant time); // 例如找出過期 license
}
