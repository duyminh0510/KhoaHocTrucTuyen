package com.duantn.repositories;

import com.duantn.entities.KetQua;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KetQuaRepository extends JpaRepository<KetQua, Integer> {

}