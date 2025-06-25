package com.duantn.repositories;

import com.duantn.entities.BaiGiang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BaiGiangRepository extends JpaRepository<BaiGiang, Integer> {
    List<BaiGiang> findByChuongs_ChuongId(Integer chuongId);
} 