package com.duantn.services;

import java.util.List;


import org.springframework.stereotype.Service;

import com.duantn.entities.DangHoc;
import com.duantn.repositories.DangHocRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DangHocService {

    private final DangHocRepository dangHocRepository;

    public List<DangHoc> layDanhSachKhoaHocDaDangKy(Integer taikhoanId) {
        return dangHocRepository.findByTaikhoanIdAndTrangthaiTrueWithKhoaHoc(taikhoanId);
    }
>>>>>>> e3902ac97b88523aa0dfd0abeed894e124cd6871
}
