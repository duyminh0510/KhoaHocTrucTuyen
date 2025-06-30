package com.duantn.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.duantn.entities.BaiGiang;

@Repository
public interface BaiGiangRepository extends JpaRepository<BaiGiang, Integer> {

    // Lấy danh sách bài giảng theo ID chương
    List<BaiGiang> findByChuongs_ChuongId(Integer chuongId);

}
