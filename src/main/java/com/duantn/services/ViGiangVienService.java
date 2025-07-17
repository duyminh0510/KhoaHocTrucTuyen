<<<<<<< HEAD
package com.duantn.services;

import com.duantn.entities.DoanhThuGiangVien;
import com.duantn.entities.RutTienGiangVien;
import com.duantn.entities.TaiKhoan;
import com.duantn.enums.TrangThaiRutTien;

import java.math.BigDecimal;
import java.util.List;

public interface ViGiangVienService {
    BigDecimal tinhSoDu(TaiKhoan giangVien);

    List<DoanhThuGiangVien> getLichSuThuNhap(TaiKhoan giangVien);

    List<RutTienGiangVien> getLichSuRutTienThanhCong(TaiKhoan giangVien);

    List<RutTienGiangVien> getYeuCauDangXuLy(TaiKhoan giangVien);

    boolean guiYeuCauRutTien(TaiKhoan giangVien, BigDecimal soTienRut);

    List<RutTienGiangVien> findRutTienTheoTrangThai(TaiKhoan giangVien, List<TrangThaiRutTien> trangThai);

}
=======
package com.duantn.services;

import com.duantn.entities.DoanhThuGiangVien;
import com.duantn.entities.RutTienGiangVien;
import com.duantn.entities.TaiKhoan;
import com.duantn.enums.TrangThaiRutTien;

import java.math.BigDecimal;
import java.util.List;

public interface ViGiangVienService {
    BigDecimal tinhSoDu(TaiKhoan giangVien);

    List<DoanhThuGiangVien> getLichSuThuNhap(TaiKhoan giangVien);

    List<RutTienGiangVien> getLichSuRutTienThanhCong(TaiKhoan giangVien);

    List<RutTienGiangVien> getYeuCauDangXuLy(TaiKhoan giangVien);

    boolean guiYeuCauRutTien(TaiKhoan giangVien, BigDecimal soTienRut);

    List<RutTienGiangVien> findRutTienTheoTrangThai(TaiKhoan giangVien, List<TrangThaiRutTien> trangThai);

}
>>>>>>> e980eb867ad2568a9f95f59345177139d7fd0014
