package com.duantn.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import com.duantn.entities.Role;
import com.duantn.entities.TaiKhoan;

public interface TaiKhoanRepository extends JpaRepository<TaiKhoan, Integer> {
    Optional<TaiKhoan> findByEmail(String email);

    //Optional<TaiKhoan> findByName(String name);

    boolean existsByEmail(String email);

    List<TaiKhoan> findByRoleRoleId(Integer role);

    List<TaiKhoan> findByRole(Role role);

}
