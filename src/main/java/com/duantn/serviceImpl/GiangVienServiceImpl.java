package com.duantn.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.duantn.entities.GiangVien;
import com.duantn.repositories.GiangVienRepository;
import com.duantn.services.GiangVienService;

@Service
public class GiangVienServiceImpl implements GiangVienService {
    @Autowired
    GiangVienRepository giangVienRepository;

    @Override
    public List<GiangVien> timKiemTheoTenIgnoreCase(String ten) {
        if (ten == null || ten.trim().isEmpty()) {
            return giangVienRepository.findAll();
        }
        return giangVienRepository.findByTenGiangVienContainingIgnoreCaseNative(ten.trim());
    }
}
