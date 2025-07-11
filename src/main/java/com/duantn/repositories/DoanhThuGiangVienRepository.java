package com.duantn.repositories;

import com.duantn.entities.DoanhThuGiangVien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface DoanhThuGiangVienRepository extends JpaRepository<DoanhThuGiangVien, Integer> {
    List<DoanhThuGiangVien> findByTaikhoanGV_TaikhoanId(Integer id);

    @Query("SELECT SUM(d.sotiennhan) FROM DoanhThuGiangVien d WHERE d.taikhoanGV.taikhoanId = :id")
    BigDecimal tongThuNhap(@Param("id") Integer id);
} 