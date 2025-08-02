package com.duantn.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.duantn.enums.TrangThaiDoanhThu;
import com.duantn.repositories.DoanhThuGiangVienRepository;
import com.duantn.services.DoanhThuGiangVienService;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class DoanhThuGiangVienServiceImpl implements DoanhThuGiangVienService {

    @Autowired
    private DoanhThuGiangVienRepository doanhThuGiangVienRepository;

    @Override
    public BigDecimal tinhDoanhThuTheoKhoangNgay(Integer taiKhoanId, LocalDateTime startDate, LocalDateTime endDate) {
        return doanhThuGiangVienRepository.tinhDoanhThuTrongKhoangNgay(taiKhoanId, startDate, endDate,
                TrangThaiDoanhThu.DA_NHAN);
    }

}