package com.duantn.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.duantn.entitys.TaiKhoan;
import com.duantn.entitys.GiangVien;

public interface GiangVienRepository extends JpaRepository<GiangVien, Integer> {
    Optional<GiangVien> findByAccounts(TaiKhoan account);
}
