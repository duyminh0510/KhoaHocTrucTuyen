package com.duantn.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import com.duantn.entities.TaiKhoan;

public interface TaiKhoanRepository extends JpaRepository<TaiKhoan, Integer> {
    Optional<TaiKhoan> findByEmail(String email);

    boolean existsByEmail(String email);

    TaiKhoan findByEmailAndPassword(String email, String password);
}
