package com.duantn.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.duantn.entities.BaiGiang;

public interface BaiGiangRepositories extends JpaRepository<BaiGiang, Integer> {

    // interface BaiGiangRepository.java
    List<BaiGiang> findByChuong_ChuongId(Integer chuongId);

}
