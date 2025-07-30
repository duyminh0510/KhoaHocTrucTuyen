package com.duantn.repositories;

import com.duantn.entities.ThuNhapNenTang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ThuNhapNenTangRepository extends JpaRepository<ThuNhapNenTang, Integer> {
    @Query("SELECT SUM(t.sotiennhan) FROM ThuNhapNenTang t")
    Double tongTienNenTang();
}