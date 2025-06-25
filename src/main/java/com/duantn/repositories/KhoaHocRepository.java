package com.duantn.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.duantn.entities.KhoaHoc;

public interface KhoaHocRepository extends JpaRepository<KhoaHoc, Integer> {

    // Tìm theo tên khóa học (tùy chọn, ví dụ)
    List<KhoaHoc> findByTenKhoaHocContainingIgnoreCase(String keyword);

    // Lọc theo trạng thái
    List<KhoaHoc> findByTrangThai(Boolean trangThai);

    // Tìm các khóa học theo giảng viên
    List<KhoaHoc> findByGiangVien_GiangvienId(Integer giangvienId);

    // Tìm các khóa học theo danh mục
    List<KhoaHoc> findByDanhMuc_DanhmucId(Integer danhmucId);

}
