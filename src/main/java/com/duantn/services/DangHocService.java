package com.duantn.services;

import com.duantn.entities.DangHoc;

public interface DangHocService {
    long demSoLuongDangKy(Integer khoaHocId);

    DangHoc findByTaiKhoanIdAndKhoaHocId(Integer taiKhoanId, Integer khoaHocId);

}
