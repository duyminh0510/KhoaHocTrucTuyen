package com.duantn.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.duantn.entities.GioHang;
import com.duantn.entities.TaiKhoan;

public interface GioHangRepository extends JpaRepository<GioHang, Integer> {
    Optional<GioHang> findByTaikhoan(TaiKhoan taiKhoan);
}