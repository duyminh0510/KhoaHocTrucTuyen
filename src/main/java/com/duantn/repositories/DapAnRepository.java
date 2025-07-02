package com.duantn.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.duantn.entities.DapAn;

import jakarta.transaction.Transactional;

public interface DapAnRepository extends JpaRepository<DapAn, Integer> {

    @Modifying
    @Transactional
    @Query("DELETE FROM DapAn d WHERE d.cauHoi.cauHoiId = :cauHoiId")
    void deleteByCauHoiId(@Param("cauHoiId") Integer cauHoiId);

}
