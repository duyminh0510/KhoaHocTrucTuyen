package com.duantn.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.duantn.entities.DanhMuc;

@Repository
public interface DanhMucRepository extends JpaRepository<DanhMuc, Integer> {

    boolean existsByTenDanhMuc(String tenDanhMuc);

    boolean existsByTenDanhMucIgnoreCase(String tenDanhMuc);

    DanhMuc findByTenDanhMucIgnoreCase(String tenDanhMuc);

    Optional<DanhMuc> findBySlug(String slug);

}