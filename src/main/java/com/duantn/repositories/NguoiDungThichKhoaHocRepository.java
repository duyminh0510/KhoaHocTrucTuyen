package com.duantn.repositories;

<<<<<<< HEAD
import com.duantn.entities.NguoiDungThichKhoaHoc;
import com.duantn.entities.TaiKhoan;
import com.duantn.entities.KhoaHoc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface NguoiDungThichKhoaHocRepository extends JpaRepository<NguoiDungThichKhoaHoc, Integer> {

    Set<NguoiDungThichKhoaHoc> findByTaiKhoan_TaikhoanId(Integer taikhoanId);

    Optional<NguoiDungThichKhoaHoc> findByTaiKhoan_TaikhoanIdAndKhoaHoc_KhoahocId(Integer taikhoanId, Integer khoahocId);
=======
import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.duantn.entities.KhoaHoc;
import com.duantn.entities.NguoiDungThichKhoaHoc;
import com.duantn.entities.TaiKhoan;

@Repository
public interface NguoiDungThichKhoaHocRepository
        extends JpaRepository<NguoiDungThichKhoaHoc, Integer> {

    Set<NguoiDungThichKhoaHoc> findByTaiKhoan_TaikhoanId(Integer taikhoanId);

    Optional<NguoiDungThichKhoaHoc> findByTaiKhoan_TaikhoanIdAndKhoaHoc_KhoahocId(
            Integer taikhoanId, Integer khoahocId);
>>>>>>> 82b8d85276debf6d30035129ac4415f6a301d0a0

    Optional<NguoiDungThichKhoaHoc> findByTaiKhoanAndKhoaHoc(TaiKhoan taiKhoan, KhoaHoc khoaHoc);
}
