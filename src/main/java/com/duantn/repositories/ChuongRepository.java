package com.duantn.repositories;

import com.duantn.entities.Chuong;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChuongRepository extends JpaRepository<Chuong, Integer> {
} 