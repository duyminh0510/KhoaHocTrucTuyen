package com.duantn.repositories;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.duantn.entities.ThuNhapNenTang;

@Repository
public interface ThuNhapNenTangRepository extends CrudRepository<ThuNhapNenTang, Integer> {

    @Query("SELECT COALESCE(SUM(t.sotiennhan), 0) FROM ThuNhapNenTang t")
    BigDecimal getTongThuNhap();

    @Query("SELECT COALESCE(SUM(tn.sotiennhan), 0) FROM ThuNhapNenTang tn WHERE tn.ngaynhan BETWEEN :start AND :end")
    BigDecimal getTongThuNhapTrongKhoang(@Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);



}
