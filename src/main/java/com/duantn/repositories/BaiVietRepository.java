package com.duantn.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.duantn.entities.BaiViet;

public interface BaiVietRepository extends JpaRepository<BaiViet, Integer> {
    Optional<BaiViet> findByBaiGiang_BaiGiangId(Integer baiGiangId);

}
