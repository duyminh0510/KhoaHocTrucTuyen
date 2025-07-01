package com.duantn.services;

import java.util.List;
import com.duantn.entities.Chuong;

public interface ChuongService {
    List<Chuong> findByKhoaHocId(Integer khoaHocId);
}
