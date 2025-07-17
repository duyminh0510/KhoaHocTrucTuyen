package com.duantn.repositories;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.duantn.entities.DoanhThuGiangVien;
import com.duantn.entities.TaiKhoan;
import com.duantn.enums.TrangThaiDoanhThu;

@Repository
public interface DoanhThuGiangVienRepository extends JpaRepository<DoanhThuGiangVien, Integer> {

        @Query("SELECT COALESCE(SUM(d.sotiennhan), 0) FROM DoanhThuGiangVien d " +
                        "WHERE d.taikhoanGV.taikhoanId = :taiKhoanId AND d.trangthai = 'DA_NHAN'")
        BigDecimal tongTienNhanTheoGiangVien(@Param("taiKhoanId") Integer taiKhoanId);

        @Query("SELECT COALESCE(SUM(d.sotiennhan), 0) " +
                        "FROM DoanhThuGiangVien d " +
                        "WHERE d.taikhoanGV.taikhoanId = :taiKhoanId " +
                        "AND d.ngaynhan BETWEEN :startDate AND :endDate " +
                        "AND d.trangthai = :trangThai")
        BigDecimal tinhDoanhThuTrongKhoangNgay(@Param("taiKhoanId") Integer taiKhoanId,
                        @Param("startDate") LocalDateTime startDate,
                        @Param("endDate") LocalDateTime endDate,
                        @Param("trangThai") TrangThaiDoanhThu trangThai);

        List<DoanhThuGiangVien> findByTaikhoanGV(TaiKhoan taiKhoan);

}
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
