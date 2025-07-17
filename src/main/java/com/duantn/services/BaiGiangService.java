package com.duantn.services;

import java.util.List;
import java.util.Optional;

import com.duantn.entities.BaiGiang;

public interface BaiGiangService {
    BaiGiang findBaiGiangById(Integer id);

    BaiGiang save(BaiGiang baiGiang);

    void xoaBaiGiangTheoId(Integer id);

    List<BaiGiang> findByChuongId(Integer chuongId);

    Optional<BaiGiang> findById(Integer id);

    BaiGiang findByTracNghiemId(Integer baiTracNghiemId);
}
