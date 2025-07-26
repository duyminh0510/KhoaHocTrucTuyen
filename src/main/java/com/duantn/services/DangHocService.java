package com.duantn.services;

import com.duantn.entities.DangHoc;

public interface DangHocService {
    long demSoLuongDangKy(Integer khoaHocId);

    DangHoc findByTaiKhoanIdAndKhoaHocId(Integer taiKhoanId, Integer khoaHocId);

    void capNhatTrangThaiVaTaoChungChi(DangHoc dangHoc);
    
    // Kiểm tra học viên đã mua khóa học hay chưa
    boolean isEnrolled(Integer taiKhoanId, Integer khoaHocId);
}
