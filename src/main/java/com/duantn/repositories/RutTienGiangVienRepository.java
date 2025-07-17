<<<<<<< HEAD
package com.duantn.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.duantn.entities.RutTienGiangVien;
import com.duantn.entities.TaiKhoan;
import com.duantn.enums.TrangThaiRutTien;

@Repository
public interface RutTienGiangVienRepository extends JpaRepository<RutTienGiangVien, Integer> {
    List<RutTienGiangVien> findByTaikhoanGVAndTrangthai(TaiKhoan taiKhoan, TrangThaiRutTien trangthai);

    List<RutTienGiangVien> findByTaikhoanGVAndTrangthaiIn(TaiKhoan giangVien, List<TrangThaiRutTien> trangThai);
}
=======
package com.duantn.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.duantn.entities.RutTienGiangVien;
import com.duantn.entities.TaiKhoan;
import com.duantn.enums.TrangThaiRutTien;

@Repository
public interface RutTienGiangVienRepository extends JpaRepository<RutTienGiangVien, Integer> {
    List<RutTienGiangVien> findByTaikhoanGVAndTrangthai(TaiKhoan taiKhoan, TrangThaiRutTien trangthai);
    List<RutTienGiangVien> findByTaikhoanGVAndTrangthaiIn(TaiKhoan giangVien, List<TrangThaiRutTien> trangThai);
}
>>>>>>> e980eb867ad2568a9f95f59345177139d7fd0014
