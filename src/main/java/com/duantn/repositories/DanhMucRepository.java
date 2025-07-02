package com.duantn.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.duantn.entities.DanhMuc;

public interface DanhMucRepository extends JpaRepository<DanhMuc, Integer> {
    boolean existsByTenDanhMucIgnoreCase(String tenDanhMuc);

    DanhMuc findByTenDanhMucIgnoreCase(String tenDanhMuc);
}
