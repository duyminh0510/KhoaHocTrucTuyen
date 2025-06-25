package com.duantn.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.duantn.entities.GiangVien;
import com.duantn.entities.TaiKhoan;

@Repository
public interface GiangVienRepository extends JpaRepository<GiangVien, Integer> {

    Optional<GiangVien> findByTaikhoan(TaiKhoan taikhoan); // phải đúng tên biến là "taikhoan"

}
