package com.duantn.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.duantn.entities.KhoaHoc;
import com.duantn.enums.TrangThaiKhoaHoc;
import com.duantn.repositories.KhoaHocRepository;
import com.duantn.services.KhoaHocService;

@Service
public class KhoaHocServiceImpl implements KhoaHocService {
    @Autowired
    KhoaHocRepository khoaHocRepository;

    @Override
    public List<KhoaHoc> getTatCaKhoaHoc() {
        return khoaHocRepository.findAll();
    }

    @Override
    public KhoaHoc getKhoaHocById(Integer Id) {
        return khoaHocRepository.findById(Id).orElse(null);
    }

    @Override
    public KhoaHoc save(KhoaHoc khoaHoc) {
        return khoaHocRepository.save(khoaHoc);
    }

    @Override
    public List<KhoaHoc> timKiemTheoTen(String tenKhoaHoc) {
        if (tenKhoaHoc == null || tenKhoaHoc.trim().isEmpty()) {
            return khoaHocRepository.findAllActive(TrangThaiKhoaHoc.PUBLISHED); // hoặc DANG_HOAT_DONG
        }
        return khoaHocRepository.findByTenKhoaHocContainingSimple(tenKhoaHoc.trim(), TrangThaiKhoaHoc.PUBLISHED);
    }

    // Tìm kiếm khóa học theo tên (không phân biệt hoa thường)
    @Override
    public List<KhoaHoc> timKiemTheoTenIgnoreCase(String tenKhoaHoc) {
        if (tenKhoaHoc == null || tenKhoaHoc.trim().isEmpty()) {
            return khoaHocRepository.findAllActive(TrangThaiKhoaHoc.PUBLISHED);

        }
        return khoaHocRepository.timTheoTenIgnoreCase(tenKhoaHoc.trim());
    }

    @Override
    public List<KhoaHoc> layTatCaKhoaHoc() {
        return khoaHocRepository.findAllActive(TrangThaiKhoaHoc.PUBLISHED);

    }

    @Override
    public List<KhoaHoc> layKhoaHocDeXuat(int soLuong) {
        List<KhoaHoc> all = khoaHocRepository.findAllActive(TrangThaiKhoaHoc.PUBLISHED);
        return all.stream().limit(soLuong).toList();
    }

}
