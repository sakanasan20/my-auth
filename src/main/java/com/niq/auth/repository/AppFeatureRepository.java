package com.niq.auth.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.niq.auth.entity.AppFeature;

@Repository
public interface AppFeatureRepository extends JpaRepository<AppFeature, Long> {

	List<AppFeature> findByModule_Id(Long moduleId);

	Optional<AppFeature> findByName(String name);

	Optional<AppFeature> findByNameAndModule_Id(String name, Long moduleId);

}
