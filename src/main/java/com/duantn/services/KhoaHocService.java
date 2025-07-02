package com.duantn.services;

import java.util.List;

import com.duantn.entities.KhoaHoc;

public interface KhoaHocService {
    List<KhoaHoc> getTatCaKhoaHoc();

    KhoaHoc getKhoaHocById(Integer Id);

    KhoaHoc save(KhoaHoc khoaHoc);

    List<KhoaHoc> timKiemTheoTenIgnoreCase(String tenKhoaHoc);

    List<KhoaHoc> timKiemTheoTen(String tenKhoaHoc);

    List<KhoaHoc> layTatCaKhoaHoc();

    List<KhoaHoc> layKhoaHocDeXuat(int soLuong);

}
