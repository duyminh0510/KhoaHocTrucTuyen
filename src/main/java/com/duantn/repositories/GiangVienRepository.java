package com.duantn.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.duantn.entities.GiangVien;
import com.duantn.entities.TaiKhoan;

public interface GiangVienRepository extends JpaRepository<GiangVien, Integer> {

        Optional<GiangVien> findByTaikhoan(TaiKhoan taikhoan);

        // Tìm kiếm giảng viên theo tên (không phân biệt hoa thường)
        @Query("SELECT gv FROM GiangVien gv WHERE LOWER(gv.taikhoan.name) LIKE LOWER(CONCAT('%', :ten, '%'))")
        java.util.List<GiangVien> findByTenGiangVienContainingIgnoreCase(
                        @org.springframework.data.repository.query.Param("ten") String ten);

        // Tìm kiếm giảng viên theo tên (native query, chắc chắn hoạt động)
        @Query(value = "SELECT * FROM GiangVien gv JOIN TaiKhoan tk ON gv.taikhoanId = tk.taikhoanId WHERE LOWER(tk.name) LIKE LOWER(CONCAT('%', :ten, '%'))", nativeQuery = true)
        java.util.List<GiangVien> findByTenGiangVienContainingIgnoreCaseNative(
                        @org.springframework.data.repository.query.Param("ten") String ten);
}
