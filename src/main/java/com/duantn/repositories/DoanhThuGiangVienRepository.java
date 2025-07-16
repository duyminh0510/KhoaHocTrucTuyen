package com.duantn.repositories;

import com.duantn.entities.DoanhThuGiangVien;
import com.duantn.entities.TaiKhoan;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DoanhThuGiangVienRepository extends JpaRepository<DoanhThuGiangVien, Integer> {
    List<DoanhThuGiangVien> findByTaikhoanGV(TaiKhoan taiKhoan);
}
