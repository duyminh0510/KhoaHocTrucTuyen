package com.duantn.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.duantn.entities.DangHoc;

public interface DangHocRepository extends JpaRepository<DangHoc, Integer> {
    @Query("SELECT dh FROM DangHoc dh JOIN FETCH dh.khoahoc WHERE dh.taikhoan.taikhoanId = :id")
    List<DangHoc> findByTaikhoanIdWithKhoaHoc(@Param("id") Integer id);

}
