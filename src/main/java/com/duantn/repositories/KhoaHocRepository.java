package com.duantn.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page; // thêm import cho phân trang
import org.springframework.data.domain.Pageable; // thêm import cho phân trang
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.duantn.entities.GiangVien;
import com.duantn.entities.KhoaHoc;
import com.duantn.enums.TrangThaiGiaoDich;
import com.duantn.enums.TrangThaiKhoaHoc;

@Repository
public interface KhoaHocRepository extends JpaRepository<KhoaHoc, Integer> {

        // --- Giữ nguyên tất cả method cũ ---

        List<KhoaHoc> findAllByTrangThai(TrangThaiKhoaHoc trangThai);

        @EntityGraph(attributePaths = {"giangVien", "giangVien.taikhoan"})
        List<KhoaHoc> findByOrderByNgayTaoDesc(Pageable pageable);

        @Query("""
                        SELECT gdct.khoahoc.khoahocId
                        FROM GiaoDichKhoaHocChiTiet gdct
                        GROUP BY gdct.khoahoc.khoahocId
                        ORDER BY COUNT(gdct.id) DESC
                        """)
        List<Integer> findTopPurchasedCourseIds(Pageable pageable);

        @Query("SELECT kh FROM KhoaHoc kh WHERE kh.khoahocId IN :ids")
        @EntityGraph(attributePaths = {"giangVien", "giangVien.taikhoan"})
        List<KhoaHoc> findByIdInWithDetails(@Param("ids") List<Integer> ids);

        @Query("SELECT kh FROM KhoaHoc kh WHERE kh.khoahocId = :id")
        @EntityGraph(attributePaths = {"giangVien.taikhoan", "danhMuc"})
        Optional<KhoaHoc> findByIdWithDetails(@Param("id") Integer id);

        @Query("SELECT n.khoaHoc FROM NguoiDungThichKhoaHoc n WHERE n.taiKhoan.taikhoanId = :taikhoanId")
        @EntityGraph(attributePaths = {"giangVien", "giangVien.taikhoan"})
        List<KhoaHoc> findLikedCoursesByAccountId(@Param("taikhoanId") Integer taikhoanId);

        @Query("""
                        SELECT DISTINCT gdct.khoahoc FROM GiaoDichKhoaHocChiTiet gdct
                        JOIN FETCH gdct.khoahoc.giangVien gv
                        JOIN FETCH gv.taikhoan
                        WHERE gdct.giaoDichKhoaHoc.taikhoan.email = :email
                        AND gdct.giaoDichKhoaHoc.trangthai = :status
                        """)
        List<KhoaHoc> findEnrolledCoursesByEmail(@Param("email") String email,
                        @Param("status") TrangThaiGiaoDich status);

        @Query("SELECT k FROM KhoaHoc k LEFT JOIN FETCH k.chuongs c LEFT JOIN FETCH c.baiGiangs WHERE k.khoahocId = :id")
        Optional<KhoaHoc> findByIdWithChaptersAndLectures(@Param("id") Integer id);

        @Query("SELECT k FROM KhoaHoc k WHERE k.tenKhoaHoc LIKE %:tuKhoa% AND k.trangThai = :trangThai")
        List<KhoaHoc> findByTenKhoaHocContainingSimple(@Param("tuKhoa") String tuKhoa,
                        @Param("trangThai") TrangThaiKhoaHoc trangThai);

        @Query("SELECT k FROM KhoaHoc k WHERE k.trangThai = :trangThai ORDER BY k.ngayTao DESC")
        List<KhoaHoc> findAllActive(@Param("trangThai") TrangThaiKhoaHoc trangThai);

        List<KhoaHoc> findByDanhMuc_danhmucId(Integer danhMucId);

        List<KhoaHoc> findByDanhMuc_danhmucIdAndTrangThai(Integer danhMucId,
                        TrangThaiKhoaHoc trangThai);

        @Query("SELECT k FROM KhoaHoc k WHERE k.danhMuc.danhmucId = :danhMucId AND k.trangThai = 'PUBLISHED'")
        List<KhoaHoc> findPublishedByDanhMucId(@Param("danhMucId") Integer danhMucId);

        @Query("""
                        SELECT k FROM KhoaHoc k
                        JOIN FETCH k.giangVien gv
                        WHERE k.trangThai = 'PUBLISHED'
                        AND (:keyword IS NULL OR LOWER(k.tenKhoaHoc) LIKE LOWER(CONCAT('%', :keyword, '%')))
                        """)
        List<KhoaHoc> timTheoTenVaTrangThaiPublished(@Param("keyword") String keyword);

        List<KhoaHoc> findByTrangThai(TrangThaiKhoaHoc trangThai);

        Optional<KhoaHoc> findBySlug(String slug);

        List<KhoaHoc> findByGiangVien(GiangVien giangVien);

        // --- THÊM PHÂN TRANG theo trạng thái ---
        Page<KhoaHoc> findByTrangThai(TrangThaiKhoaHoc trangThai, Pageable pageable);
}
