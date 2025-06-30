package com.duantn.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.duantn.entities.BaiGiang;
import com.duantn.repositories.BaiGiangRepository;

@Service
public class BaiGiangServiceImpl implements BaiGiangService {

    @Autowired
    private BaiGiangRepository baiGiangRepository;

    @Override
    public BaiGiang findById(Integer id) {
        return baiGiangRepository.findById(id).orElse(null);
    }
}
