package com.duantn.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.duantn.entities.KhoaHoc;
import com.duantn.repositories.KhoaHocRepository;

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
}
