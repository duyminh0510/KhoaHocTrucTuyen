package com.duantn.services;

import java.util.List;
import com.duantn.entitys.DanhMuc;

public interface DanhMucService {
    List<DanhMuc> findAll();

    DanhMuc save(DanhMuc danhMuc);

    void deleteById(Integer id);

    DanhMuc findById(Integer id);
}
