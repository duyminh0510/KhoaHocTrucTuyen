package com.duantn.services;

import java.util.List;

import com.duantn.entities.KhoaHoc;

public interface DangHocService {
    List<KhoaHoc> layDanhSachKhoaHocTheoTaiKhoan(Integer taikhoanId);
}
