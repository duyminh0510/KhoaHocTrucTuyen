package com.duantn.repositories;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.duantn.dtos.HocVienTheoKhoaHocDto;
import com.duantn.entities.DangHoc;
import com.duantn.entities.GiangVien;
import com.duantn.entities.KhoaHoc;

@Repository
public interface DangHocRepository extends JpaRepository<DangHoc, Integer> {
    @Query("SELECT dh FROM DangHoc dh JOIN FETCH dh.khoahoc WHERE dh.taikhoan.taikhoanId = :id")
    List<DangHoc> findByTaikhoanIdWithKhoaHoc(@Param("id") Integer id);

    long countByKhoahoc_KhoahocId(Integer khoahocId);

    List<DangHoc> findByKhoahoc(KhoaHoc khoaHoc);

    List<DangHoc> findByKhoahocIn(List<KhoaHoc> khoaHocList); // <- Thêm dòng này

    DangHoc findByTaikhoan_TaikhoanIdAndKhoahoc_KhoahocId(Integer taiKhoanId, Integer khoaHocId);

    @Query("""
                SELECT new com.duantn.dtos.HocVienTheoKhoaHocDto(
                    kh.tenKhoaHoc,
                    COUNT(dh)
                )
                FROM DangHoc dh
                JOIN dh.khoahoc kh
                WHERE kh.giangVien.giangvienId = :giangVienId
                GROUP BY kh.tenKhoaHoc
            """)
    List<HocVienTheoKhoaHocDto> demSoHocVienTheoKhoaHoc(@Param("giangVienId") Integer giangVienId);

    // tổng hoc viên đã đăng ký
    @Query("SELECT COUNT(DISTINCT dh.taikhoan.taikhoanId) " + "FROM DangHoc dh "
            + "JOIN dh.khoahoc kh " + "JOIN kh.giangVien gv "
            + "WHERE gv.giangvienId = :giangVienId")
    long demSoHocVienTheoGiangVien(@Param("giangVienId") Integer giangVienId);

    boolean existsByKhoahoc_KhoahocId(Integer khoahocId);

    //
    @Query("SELECT d.khoahoc.tenKhoaHoc, COUNT(d) " + "FROM DangHoc d "
            + "WHERE d.khoahoc.giangVien = :giangVien " + "GROUP BY d.khoahoc.tenKhoaHoc "
            + "ORDER BY COUNT(d) DESC")
    List<Object[]> findTop5ByGiangVien(@Param("giangVien") GiangVien giangVien, Pageable pageable);

}
