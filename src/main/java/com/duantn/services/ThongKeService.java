package com.duantn.services;

import java.util.List;
import java.util.Map;

public interface ThongKeService {
    int tongHocVien();

    int tongGiangVien();

    int tongKhoaHoc();

    double doanhThuThangNay();

    List<Double> getDoanhThu6Thang();

    Map<String, Integer> getTiLeDanhMuc();

    List<Object> getChiTietKhoaHoc(); // Có thể tạo DTO riêng nếu cần
}