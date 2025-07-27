package com.duantn.repositories;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.duantn.entities.ThuNhapNenTang;

public interface ThuNhapNenTangRepository extends JpaRepository<ThuNhapNenTang, Integer> {

    @Query("SELECT COALESCE(SUM(t.sotiennhan), 0) FROM ThuNhapNenTang t")
    BigDecimal getTongThuNhap();

    @Query("SELECT COALESCE(SUM(t.sotiennhan), 0) FROM ThuNhapNenTang t WHERE t.ngaynhan BETWEEN :start AND :end")
    BigDecimal getTongThuNhapTrongKhoang(LocalDateTime start, LocalDateTime end);

    Page<ThuNhapNenTang> findByNgaynhanBetween(LocalDateTime start, LocalDateTime end,
            Pageable pageable);
}
