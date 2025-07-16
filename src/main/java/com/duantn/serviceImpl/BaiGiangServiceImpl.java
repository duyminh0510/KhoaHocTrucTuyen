package com.duantn.serviceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.duantn.entities.BaiGiang;
import com.duantn.repositories.BaiGiangRepositories;
import com.duantn.services.BaiGiangService;

@Service
public class BaiGiangServiceImpl implements BaiGiangService {
    @Autowired
    BaiGiangRepositories baiGiangRepositories;

    @Override
    public BaiGiang findBaiGiangById(Integer id) {
        return baiGiangRepositories.findById(id).orElse(null);
    }

    @Override
    public BaiGiang save(BaiGiang baiGiang) {
        return baiGiangRepositories.save(baiGiang);
    }

    @Override
    public void xoaBaiGiangTheoId(Integer id) {
        BaiGiang baiGiang = baiGiangRepositories.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy bài giảng với ID: " + id));
        baiGiangRepositories.delete(baiGiang);
    }

    @Override
    public List<BaiGiang> findByChuongId(Integer chuongId) {
        return baiGiangRepositories.findByChuong_ChuongId(chuongId);
    }

    @Override
    public Optional<BaiGiang> findById(Integer id) {
        return baiGiangRepositories.findById(id);
    }

}
