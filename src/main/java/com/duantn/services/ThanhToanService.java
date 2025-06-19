package com.duantn.services;

import com.duantn.entitys.ThanhToan;
import java.util.List;

public interface ThanhToanService {
    List<ThanhToan> findAll();
    ThanhToan findById(Integer id);
    ThanhToan save(ThanhToan thanhToan);
    void deleteById(Integer id);
} 