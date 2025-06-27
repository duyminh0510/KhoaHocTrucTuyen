package com.duantn.repositories;

import com.duantn.entities.KhoaHoc;
import com.duantn.enums.TrangThaiGiaoDich;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface KhoaHocRepository extends JpaRepository<KhoaHoc, Integer> {
    @EntityGraph(attributePaths = {"giangVien", "giangVien.taikhoan"})
    List<KhoaHoc> findByOrderByNgayTaoDesc(Pageable pageable);

    @Query("SELECT kh.khoahocId FROM KhoaHoc kh JOIN kh.giaoDichChiTiet gdct GROUP BY kh.khoahocId ORDER BY COUNT(gdct.id) DESC")
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

    @Query("SELECT DISTINCT gdct.khoahoc FROM GiaoDichKhoaHocChiTiet gdct " +
           "JOIN FETCH gdct.khoahoc.giangVien gv " +
           "JOIN FETCH gv.taikhoan " +
           "WHERE gdct.giaoDichKhoaHoc.taikhoan.email = :email " +
           "AND gdct.giaoDichKhoaHoc.trangthai = :status")
    List<KhoaHoc> findEnrolledCoursesByEmail(@Param("email") String email, @Param("status") TrangThaiGiaoDich status);
} 