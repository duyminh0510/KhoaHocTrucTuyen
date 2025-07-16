package com.duantn.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.duantn.entities.CauHoi;

public interface CauHoiRepository extends JpaRepository<CauHoi, Integer> {
    List<CauHoi> findAllByBaiTracNghiem_TracnghiemId(Integer tracnghiemId);

}
