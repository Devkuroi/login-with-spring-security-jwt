package com.practice.securitypractice.persistence.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.practice.securitypractice.persistence.entities.PermissionEntity;


@Repository
public interface PermissionRespository extends JpaRepository<PermissionEntity, Long> {
    List<PermissionEntity> findPermissionEntitiesByNameIn(List<String> name);

    PermissionEntity findByName(String name);
}
