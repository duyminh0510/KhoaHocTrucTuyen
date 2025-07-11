package com.duantn.repositories;

import com.duantn.entities.GiaoDichKhoaHoc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface GiaoDichKhoaHocRepository extends JpaRepository<GiaoDichKhoaHoc, Integer> {
    List<GiaoDichKhoaHoc> findByTaikhoan_TaikhoanId(Integer taiKhoanId);

    @Query("SELECT DISTINCT gd FROM GiaoDichKhoaHoc gd LEFT JOIN FETCH gd.chiTietGiaoDich ctd LEFT JOIN FETCH ctd.khoahoc kh ORDER BY gd.ngayGiaoDich DESC")
    List<GiaoDichKhoaHoc> findAllWithDetails();

    @Query("SELECT gd FROM GiaoDichKhoaHoc gd LEFT JOIN FETCH gd.chiTietGiaoDich ctd LEFT JOIN FETCH ctd.khoahoc kh WHERE gd.giaodichId = :id")
    Optional<GiaoDichKhoaHoc> findByIdWithDetails(@Param("id") Integer id);
} 