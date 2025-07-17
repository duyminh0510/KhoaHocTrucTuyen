package com.duantn.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.duantn.entities.KetQuaChiTiet;

public interface KetQuaChiTietRepository extends JpaRepository<KetQuaChiTiet, Integer> {
    List<KetQuaChiTiet> findByKetQua_KetquaId(Integer ketquaId);

    void deleteByKetQua_KetquaId(Integer ketquaId);

}
