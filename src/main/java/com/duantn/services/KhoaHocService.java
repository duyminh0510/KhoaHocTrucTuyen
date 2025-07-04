package com.duantn.services;

import java.util.List;
import java.util.Optional;

import com.duantn.entities.KhoaHoc;

public interface KhoaHocService {
    List<KhoaHoc> getTatCaKhoaHoc();

    KhoaHoc getKhoaHocById(Integer id);

    List<KhoaHoc> layTatCaKhoaHocCanDuyet();

    List<KhoaHoc> getNewestCourses(int count);

    List<KhoaHoc> getTopPurchasedCourses(int count);

    List<KhoaHoc> getEnrolledCourses(String email);

    boolean toggleLike(Integer khoahocId, Integer taikhoanId);

    List<KhoaHoc> findLikedCoursesByAccountId(Integer currentUserId);

    KhoaHoc getKhoaHocByIdWithDetails(Integer id);

    Optional<KhoaHoc> findById(Integer id);

    //

    KhoaHoc save(KhoaHoc khoaHoc);

    List<KhoaHoc> timKiemTheoTenIgnoreCase(String tenKhoaHoc);

    List<KhoaHoc> timKiemTheoTen(String tenKhoaHoc);

    List<KhoaHoc> layKhoaHocDeXuat(int soLuong);
}
