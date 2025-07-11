package com.duantn.repositories;

import com.duantn.entities.RutTienGiangVien;
import com.duantn.enums.TrangThaiRutTien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface RutTienGiangVienRepository extends JpaRepository<RutTienGiangVien, Integer> {
    List<RutTienGiangVien> findByTaikhoanGV_TaikhoanId(Integer id);

    @Query("SELECT SUM(r.soTienRut) FROM RutTienGiangVien r WHERE r.taikhoanGV.taikhoanId = :id AND r.trangthai = 'DA_RUT'")
    BigDecimal tongRutThanhCong(@Param("id") Integer id);

    List<RutTienGiangVien> findByTaikhoanGV_TaikhoanIdAndTrangthai(Integer id, TrangThaiRutTien trangthai);
} 