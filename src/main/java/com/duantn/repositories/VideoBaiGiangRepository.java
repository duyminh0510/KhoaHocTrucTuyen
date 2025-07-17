<<<<<<< HEAD
package com.duantn.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.duantn.entities.BaiGiang;
import com.duantn.entities.VideoBaiGiang;

@Repository
public interface VideoBaiGiangRepository extends JpaRepository<VideoBaiGiang, Integer> {

    VideoBaiGiang findByBaiGiang_BaiGiangId(Integer baiGiangId);

    // interface VideoBaiGiangRepository.java
    VideoBaiGiang findByBaiGiang(BaiGiang baiGiang);

}
=======
package com.duantn.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.duantn.entities.BaiGiang;
import com.duantn.entities.VideoBaiGiang;

public interface VideoBaiGiangRepository extends JpaRepository<VideoBaiGiang, Integer> {

    VideoBaiGiang findByBaiGiang_BaiGiangId(Integer baiGiangId);

    // interface VideoBaiGiangRepository.java
    VideoBaiGiang findByBaiGiang(BaiGiang baiGiang);

}
>>>>>>> e980eb867ad2568a9f95f59345177139d7fd0014
