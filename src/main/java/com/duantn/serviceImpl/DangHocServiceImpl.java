package com.duantn.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.duantn.repositories.DangHocRepository;
import com.duantn.services.DangHocService;

@Service
public class DangHocServiceImpl implements DangHocService {

    @Autowired
    private DangHocRepository dangHocRepository;

    @Override
    public long demSoLuongDangKy(Integer khoaHocId) {
        return dangHocRepository.countByKhoahoc_KhoahocId(khoaHocId);
    }

}
