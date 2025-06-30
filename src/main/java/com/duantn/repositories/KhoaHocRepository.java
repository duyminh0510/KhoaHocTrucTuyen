package com.duantn.repositories;

import com.duantn.entities.KhoaHoc;
import com.duantn.enums.TrangThaiKhoaHoc;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KhoaHocRepository extends JpaRepository<KhoaHoc, Integer> {
    List<KhoaHoc> findAllByTrangThai(TrangThaiKhoaHoc trangThai); // optional: lọc theo trạng thái
    
} 