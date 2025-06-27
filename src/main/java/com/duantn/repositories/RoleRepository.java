package com.duantn.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.duantn.entities.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(String name);

}
