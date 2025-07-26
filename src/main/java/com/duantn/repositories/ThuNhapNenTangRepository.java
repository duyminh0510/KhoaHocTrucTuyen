package com.duantn.repositories;

import java.math.BigDecimal;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import com.duantn.entities.ThuNhapNenTang;

public interface ThuNhapNenTangRepository extends CrudRepository<ThuNhapNenTang, Long> {

    @Query("SELECT COALESCE(SUM(t.sotiennhan), 0) FROM ThuNhapNenTang t")
    BigDecimal getTongThuNhap();

}
