package com.duantn.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.duantn.entities.Chuong;
import com.duantn.repositories.ChuongRepository;

@Service
public class ChuongServiceImpl implements ChuongService {

    @Autowired
    private ChuongRepository chuongRepository;

    @Override
    public List<Chuong> findByKhoaHocId(Integer khoaHocId) {
        return chuongRepository.findByKhoahoc_KhoahocId(khoaHocId);
    }

}
