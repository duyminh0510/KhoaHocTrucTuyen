package com.duantn.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.duantn.entitys.TaiKhoan;

public interface TaiKhoanRepository extends JpaRepository<TaiKhoan, Integer> {
    Optional<TaiKhoan> findByEmail(String email);

    boolean existsByEmail(String email);
}
