package com.duantn.services;

import com.duantn.entities.KhoaHoc;
import com.duantn.enums.TrangThaiKhoaHoc;
import com.duantn.repositories.KhoaHocRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KhoaHocService {
    @Autowired
    private KhoaHocRepository khoaHocRepository;

    public List<KhoaHoc> getTatCaKhoaHoc() {
        return khoaHocRepository.findAll();
    }

    public KhoaHoc getKhoaHocById(Integer id) {
        return khoaHocRepository.findById(id).orElse(null);
    }

     public List<KhoaHoc> layTatCaKhoaHocCanDuyet() {
        return khoaHocRepository.findAllByTrangThai(TrangThaiKhoaHoc.PENDING_APPROVAL); 
    }

} 