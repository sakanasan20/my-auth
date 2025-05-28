package com.niq.auth.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.niq.auth.entity.AppModule;

@Repository
public interface AppModuleRepository extends JpaRepository<AppModule, Long> {

    List<AppModule> findBySystem_Id(Long systemId); // 查詢某系統下的所有模組

    Optional<AppModule> findByName(String name);
    
    Optional<AppModule> findByNameAndSystem_Id(String name, Long systemId);
    
}
