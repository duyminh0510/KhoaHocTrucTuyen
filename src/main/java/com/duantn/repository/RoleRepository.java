package com.duantn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.duantn.entitys.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {
}
