package com.duantn.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.duantn.entities.Chuong;

@Repository
public interface ChuongRepository extends JpaRepository<Chuong, Integer> {
}
