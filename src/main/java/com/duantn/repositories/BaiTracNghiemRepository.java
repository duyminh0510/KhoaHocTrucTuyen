package com.duantn.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.duantn.entities.BaiTracNghiem;

public interface BaiTracNghiemRepository extends JpaRepository<BaiTracNghiem, Integer> {
    BaiTracNghiem findByBaiGiang_BaiGiangId(Integer baiGiangId);
}
