package com.duantn.repository;

import com.duantn.entitys.ThanhToan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ThanhToanRepository extends JpaRepository<ThanhToan, Integer> {
    // Có thể thêm các phương thức tìm kiếm nếu cần
} 