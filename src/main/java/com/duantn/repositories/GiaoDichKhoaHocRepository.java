package com.duantn.repositories;

import com.duantn.entities.GiaoDichKhoaHoc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface GiaoDichKhoaHocRepository extends JpaRepository<GiaoDichKhoaHoc, Integer> {
    List<GiaoDichKhoaHoc> findByTaikhoan_TaikhoanId(Integer taikhoanId);

    @Query("SELECT SUM(gd.tongtien) FROM GiaoDichKhoaHoc gd WHERE MONTH(gd.ngayGiaoDich) = MONTH(CURRENT_DATE) AND YEAR(gd.ngayGiaoDich) = YEAR(CURRENT_DATE)")
    Double doanhThuThangNay();

    @Query("SELECT MONTH(gd.ngayGiaoDich), SUM(gd.tongtien) FROM GiaoDichKhoaHoc gd WHERE gd.ngayGiaoDich >= :startDate GROUP BY MONTH(gd.ngayGiaoDich) ORDER BY MONTH(gd.ngayGiaoDich)")
    List<Object[]> doanhThu6ThangGanNhat(@Param("startDate") LocalDateTime startDate);
}