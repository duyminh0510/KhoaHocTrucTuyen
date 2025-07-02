package com.duantn.repositories;

import com.duantn.entities.KhoaHoc;
import com.duantn.enums.TrangThaiKhoaHoc;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface KhoaHocRepository extends JpaRepository<KhoaHoc, Integer> {

    // Tìm kiếm khóa học theo tên (không phân biệt hoa thường)
    @Query("SELECT k FROM KhoaHoc k WHERE LOWER(k.tenKhoaHoc) LIKE LOWER(CONCAT('%', :tenKhoaHoc, '%'))")
    List<KhoaHoc> timTheoTenIgnoreCase(@Param("tenKhoaHoc") String tenKhoaHoc);

    @Query("SELECT k FROM KhoaHoc k WHERE k.tenKhoaHoc LIKE %:tuKhoa% AND k.trangThai = :trangThai")
    List<KhoaHoc> findByTenKhoaHocContainingSimple(@Param("tuKhoa") String tuKhoa,
            @Param("trangThai") TrangThaiKhoaHoc trangThai);

    @Query("SELECT k FROM KhoaHoc k WHERE k.trangThai = :trangThai ORDER BY k.ngayTao DESC")
    List<KhoaHoc> findAllActive(@Param("trangThai") TrangThaiKhoaHoc trangThai);

}
