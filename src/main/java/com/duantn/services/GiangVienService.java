package com.duantn.services;

import java.util.List;

import com.duantn.entities.GiangVien;

public interface GiangVienService {
    List<GiangVien> timKiemTheoTenIgnoreCase(String ten);
}
