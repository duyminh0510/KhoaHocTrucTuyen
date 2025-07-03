package com.duantn.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.duantn.entities.BaiGiang;
import com.duantn.repositories.BaiGiangRepository;
import com.duantn.services.BaiGiangService;

@Service
public class BaiGiangServiceImpl implements BaiGiangService {

    @Autowired
    private BaiGiangRepository baiGiangRepository;

    @Override
    public BaiGiang findById(Integer id) {
        return baiGiangRepository.findById(id).orElse(null);
    }
}
