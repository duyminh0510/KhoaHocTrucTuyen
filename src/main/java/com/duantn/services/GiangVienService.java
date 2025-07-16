package com.duantn.services;

import java.util.List;

import com.duantn.entities.GiangVien;
import com.duantn.entities.TaiKhoan;
import com.duantn.dtos.DoanhThuKhoaHocGiangVienDto;

public interface GiangVienService {
    List<GiangVien> timKiemTheoTenIgnoreCase(String ten);

    List<DoanhThuKhoaHocGiangVienDto> thongKeDoanhThuTheoGiangVien(Integer giangVienId);

    GiangVien findByTaikhoan(TaiKhoan taiKhoan);

    double tinhDiemDanhGiaTrungBinh(Integer giangVienId);

    double tinhDoanhThuThangNay(Integer giangVienId);

}
