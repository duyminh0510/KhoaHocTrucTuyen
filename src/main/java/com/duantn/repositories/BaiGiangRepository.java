package com.duantn.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.duantn.entities.BaiGiang;
import com.duantn.enums.LoaiBaiGiang;

@Repository
public interface BaiGiangRepository extends JpaRepository<BaiGiang, Integer> {

    // Tìm tất cả bài giảng thuộc một chương
    List<BaiGiang> findByChuongs_ChuongId(Integer chuongId);

    // Tìm theo loại bài giảng
    List<BaiGiang> findByLoaiBaiGiang(LoaiBaiGiang loaiBaiGiang);

    // Tìm theo chương và loại bài giảng
    List<BaiGiang> findByChuongs_ChuongIdAndLoaiBaiGiang(Integer chuongId,
            LoaiBaiGiang loaiBaiGiang);

    // Tìm theo trạng thái
    List<BaiGiang> findByTrangthai(Boolean trangthai);

    // Kết hợp chương, loại bài giảng và trạng thái
    List<BaiGiang> findByChuongs_ChuongIdAndLoaiBaiGiangAndTrangthai(Integer chuongId,
            LoaiBaiGiang loaiBaiGiang, Boolean trangthai);
}
