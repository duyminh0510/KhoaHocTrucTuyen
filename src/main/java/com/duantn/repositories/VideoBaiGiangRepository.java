package com.duantn.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.duantn.entities.BaiGiang;
import com.duantn.entities.VideoBaiGiang;

public interface VideoBaiGiangRepository extends JpaRepository<VideoBaiGiang, Integer> {

    VideoBaiGiang findByBaiGiang_BaiGiangId(Integer baiGiangId);

    // interface VideoBaiGiangRepository.java
    VideoBaiGiang findByBaiGiang(BaiGiang baiGiang);

}
