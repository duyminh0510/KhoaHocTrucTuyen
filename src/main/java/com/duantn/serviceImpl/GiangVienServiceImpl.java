package com.duantn.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.LocalTime;

import org.springframework.transaction.annotation.Transactional;
import com.duantn.entities.GiangVien;
import com.duantn.entities.TaiKhoan;
import com.duantn.repositories.GiangVienRepository;
import com.duantn.services.GiangVienService;
import com.duantn.dtos.DoanhThuKhoaHocGiangVienDto;

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
    public List<DoanhThuKhoaHocGiangVienDto> thongKeDoanhThuTheoGiangVien(Integer giangVienId) {
        return giangVienRepository.thongKeDoanhThuTheoGiangVien(giangVienId);
    }

    @Override
    public GiangVien findByTaikhoan(TaiKhoan taiKhoan) {
        return giangVienRepository.findByTaikhoan(taiKhoan).orElse(null);
    }

    @Transactional
    @Override
    public double tinhDiemDanhGiaTrungBinh(Integer giangVienId) {
        Double diemTB = giangVienRepository.tinhDiemDanhGiaTrungBinh(giangVienId);
        return diemTB != null ? diemTB : 0.0;
    }

    @Transactional
    @Override
    public double tinhDoanhThuThangNay(Integer giangVienId) {
        // Lấy ngày đầu và cuối của tháng hiện tại
        YearMonth currentMonth = YearMonth.now();
        LocalDate start = currentMonth.atDay(1);
        LocalDate end = currentMonth.atEndOfMonth();

        LocalDateTime startDateTime = start.atStartOfDay();
        LocalDateTime endDateTime = end.atTime(LocalTime.MAX);

        Double doanhThu = giangVienRepository.tinhDoanhThuTrongKhoangThoiGian(giangVienId, startDateTime, endDateTime);
        return doanhThu != null ? doanhThu : 0.0;
    }

}
