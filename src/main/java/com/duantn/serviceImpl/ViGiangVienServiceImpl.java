package com.duantn.serviceImpl;

import com.duantn.entities.DoanhThuGiangVien;
import com.duantn.entities.RutTienGiangVien;
import com.duantn.enums.TrangThaiRutTien;
import com.duantn.repositories.DoanhThuGiangVienRepository;
import com.duantn.repositories.RutTienGiangVienRepository;
import com.duantn.services.ViGiangVienService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ViGiangVienServiceImpl implements ViGiangVienService {

    @Autowired
    private DoanhThuGiangVienRepository doanhThuGiangVienRepository;

    @Autowired
    private RutTienGiangVienRepository rutTienGiangVienRepository;

    @Override
    public BigDecimal tinhSoDu(Integer giangVienId) {
        BigDecimal tongThuNhap = doanhThuGiangVienRepository.tongThuNhap(giangVienId);
        BigDecimal tongRutThanhCong = rutTienGiangVienRepository.tongRutThanhCong(giangVienId);

        if (tongThuNhap == null) {
            tongThuNhap = BigDecimal.ZERO;
        }
        if (tongRutThanhCong == null) {
            tongRutThanhCong = BigDecimal.ZERO;
        }

        return tongThuNhap.subtract(tongRutThanhCong);
    }

    @Override
    public List<DoanhThuGiangVien> lichSuThuNhap(Integer giangVienId) {
        return doanhThuGiangVienRepository.findByTaikhoanGV_TaikhoanId(giangVienId);
    }

    @Override
    public List<RutTienGiangVien> lichSuRutTien(Integer giangVienId) {
        return rutTienGiangVienRepository.findByTaikhoanGV_TaikhoanId(giangVienId);
    }

    @Override
    public List<RutTienGiangVien> yeuCauDangXuLy(Integer giangVienId) {
        return rutTienGiangVienRepository.findByTaikhoanGV_TaikhoanIdAndTrangthai(giangVienId, TrangThaiRutTien.DAGUI_YEUCAU);
    }

    @Override
    public void luuYeuCauRutTien(RutTienGiangVien yeuCau) {
        rutTienGiangVienRepository.save(yeuCau);
    }
} 