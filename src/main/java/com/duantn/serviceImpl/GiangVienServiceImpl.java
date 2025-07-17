package com.duantn.serviceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.duantn.entities.GiangVien;
import com.duantn.entities.TaiKhoan;
import com.duantn.repositories.GiangVienRepository;
import com.duantn.services.GiangVienService;

@Service
public class GiangVienServiceImpl implements GiangVienService {
    @Autowired
    GiangVienRepository giangVienRepository;

    @Override
    public List<GiangVien> timKiemTheoTenIgnoreCase(String ten) {
        if (ten == null || ten.trim().isEmpty()) {
            return giangVienRepository.findAll();
        }
        return giangVienRepository.findByTenGiangVienContainingIgnoreCaseNative(ten.trim());
    }

    @Override
    public GiangVien getByTaiKhoanId(Integer taiKhoanId) {
        return giangVienRepository.findByTaikhoan_TaikhoanId(taiKhoanId).orElse(null);
    }

    @Override
    public boolean capNhatThongTinNganHang(Integer giangVienId, String soTaiKhoan, String tenNganHang) {
        Optional<GiangVien> optionalGV = giangVienRepository.findById(giangVienId);
        if (optionalGV.isPresent()) {
            GiangVien gv = optionalGV.get();
            // gv.setSoTaiKhoan(soTaiKhoan);
            // gv.setTenNganHang(tenNganHang);
            giangVienRepository.save(gv);
            return true;
        }
        return false;
    }

    @Override
    public GiangVien getByTaiKhoan(TaiKhoan taiKhoan) {
        return giangVienRepository.getByTaikhoan(taiKhoan);
    }

    @Override
    public GiangVien getById(Integer id) {
        return giangVienRepository.findById(id).orElse(null);
    }

}
