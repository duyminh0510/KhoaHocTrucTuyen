package com.duantn.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.duantn.entities.GiangVien;
import com.duantn.entities.TaiKhoan;

public interface GiangVienRepository extends JpaRepository<GiangVien, Integer> {
    Optional<GiangVien> findByAccounts(TaiKhoan account);
}
