package com.duantn.services;

import com.duantn.entities.DoanhThuGiangVien;
import com.duantn.entities.RutTienGiangVien;

import java.math.BigDecimal;
import java.util.List;

public interface ViGiangVienService {
    BigDecimal tinhSoDu(Integer giangVienId);
    List<DoanhThuGiangVien> lichSuThuNhap(Integer giangVienId);
    List<RutTienGiangVien> lichSuRutTien(Integer giangVienId);
    List<RutTienGiangVien> yeuCauDangXuLy(Integer giangVienId);
    void luuYeuCauRutTien(RutTienGiangVien yeuCau);
} 