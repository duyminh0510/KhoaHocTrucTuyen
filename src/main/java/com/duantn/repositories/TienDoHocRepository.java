package com.duantn.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.duantn.entities.BaiGiang;
import com.duantn.entities.DangHoc;
import com.duantn.entities.TienDoHoc;

@Repository
public interface TienDoHocRepository extends JpaRepository<TienDoHoc, Integer> {
    List<TienDoHoc> findByDangHoc_DanghocId(Integer danghocId);

    boolean existsByDangHocAndBaiGiang(DangHoc dangHoc, BaiGiang baiGiang);

    TienDoHoc findByDangHoc_DanghocIdAndBaiGiang_BaiGiangId(Integer dangHocId, Integer baiGiangId);

}