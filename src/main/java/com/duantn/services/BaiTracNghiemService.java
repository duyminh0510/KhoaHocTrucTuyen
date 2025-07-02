package com.duantn.services;

import com.duantn.entities.BaiGiang;
import com.duantn.entities.BaiTracNghiem;

public interface BaiTracNghiemService {

    BaiTracNghiem save(BaiTracNghiem tracNghiem);

    BaiTracNghiem findByBaiGiangId(Integer baiGiangId);

    void saveBaiTracNghiemVaCauHoi(BaiTracNghiem tracMoi, BaiGiang baiGiang);

}
