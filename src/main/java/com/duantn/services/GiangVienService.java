package com.duantn.services;

import java.util.List;

import com.duantn.entities.GiangVien;
import com.duantn.entities.TaiKhoan;

public interface GiangVienService {
    List<GiangVien> timKiemTheoTenIgnoreCase(String ten);
    GiangVien getByTaiKhoanId(Integer taiKhoanId);
    boolean capNhatThongTinNganHang(Integer giangVienId, String soTaiKhoan, String tenNganHang);
    GiangVien getByTaiKhoan(TaiKhoan taiKhoan);
    GiangVien getById(Integer id);
}
