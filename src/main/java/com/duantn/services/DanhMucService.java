package com.duantn.services;

import java.util.List;

import com.duantn.entities.DanhMuc;

public interface DanhMucService {
    List<DanhMuc> findAll();

    DanhMuc save(DanhMuc danhMuc);

    void deleteById(Integer id);

    DanhMuc findById(Integer id);
}
