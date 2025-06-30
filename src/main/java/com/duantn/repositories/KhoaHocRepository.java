package com.duantn.repositories;

import com.duantn.entities.KhoaHoc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface KhoaHocRepository extends JpaRepository<KhoaHoc, Integer> {

    @Query("SELECT k FROM KhoaHoc k " +
           "LEFT JOIN FETCH k.chuongs c " +
           "LEFT JOIN FETCH c.baiGiangs " +
           "WHERE k.khoahocId = :id")
    Optional<KhoaHoc> findByIdWithChaptersAndLectures(@Param("id") Integer id);
} 