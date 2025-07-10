package com.duantn.repositories;

import com.duantn.entities.GiaoDichKhoaHoc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GiaoDichKhoaHocRepository extends JpaRepository<GiaoDichKhoaHoc, Integer> {
}