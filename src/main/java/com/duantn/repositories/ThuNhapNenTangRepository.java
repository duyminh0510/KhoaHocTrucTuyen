package com.duantn.repositories;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import com.duantn.entities.ThuNhapNenTang;

public interface ThuNhapNenTangRepository extends CrudRepository<ThuNhapNenTang, Long> {

    @Query("SELECT COALESCE(SUM(t.sotiennhan), 0) FROM ThuNhapNenTang t")
    BigDecimal getTongThuNhap();

    @Query("SELECT SUM(tn.sotiennhan) FROM ThuNhapNenTang tn WHERE tn.ngaynhan >= :start AND tn.ngaynhan <= :end")
    BigDecimal getTongThuNhapTrongKhoang(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}
