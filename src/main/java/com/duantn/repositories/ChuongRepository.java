package com.duantn.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.duantn.entities.Chuong;

public interface ChuongRepository extends JpaRepository<Chuong, Integer> {

    // Lấy danh sách chương theo khoahocId và trạng thái
    List<Chuong> findByKhoahoc_KhoahocIdAndTrangthai(Integer khoahocId, Boolean trangthai);

    // Nếu chỉ cần theo khoahocId
    List<Chuong> findByKhoahoc_KhoahocId(Integer khoahocId);
}
