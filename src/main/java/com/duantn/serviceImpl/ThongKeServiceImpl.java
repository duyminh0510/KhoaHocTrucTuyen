package com.duantn.serviceImpl;

import com.duantn.services.ThongKeService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.duantn.repositories.*;
import java.util.*;
import java.time.*;
import java.math.BigDecimal;

@Service
public class ThongKeServiceImpl implements ThongKeService {
    @Autowired
    private TaiKhoanRepository taiKhoanRepository;
    @Autowired
    private GiangVienRepository giangVienRepository;
    @Autowired
    private KhoaHocRepository khoaHocRepository;
    @Autowired
    private GiaoDichKhoaHocRepository giaoDichKhoaHocRepository;
    @Autowired
    private ThuNhapNenTangRepository thuNhapNenTangRepository;

    @Override
    public int tongHocVien() {
        return taiKhoanRepository.countHocVien();
    }

    @Override
    public int tongGiangVien() {
        return giangVienRepository.countGiangVien();
    }

    @Override
    public int tongKhoaHoc() {
        return khoaHocRepository.countKhoaHoc();
    }

    @Override
    public double doanhThuThangNay() {
        Double result = giaoDichKhoaHocRepository.doanhThuThangNay();
        return result != null ? result : 0.0;
    }

    @Override
    public List<Double> getDoanhThu6Thang() {
        LocalDateTime startDate = LocalDate.now().minusMonths(5).withDayOfMonth(1).atStartOfDay();
        List<Object[]> raw = giaoDichKhoaHocRepository.doanhThu6ThangGanNhat(startDate);
        Map<Integer, Double> monthToRevenue = new HashMap<>();
        for (Object[] row : raw) {
            monthToRevenue.put(((Integer) row[0]), row[1] != null ? ((BigDecimal) row[1]).doubleValue() : 0.0);
        }
        List<Double> result = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            int month = LocalDate.now().minusMonths(5 - i).getMonthValue();
            result.add(monthToRevenue.getOrDefault(month, 0.0));
        }
        return result;
    }

    @Override
    public Map<String, Integer> getTiLeDanhMuc() {
        List<Object[]> raw = khoaHocRepository.tiLeDanhMuc();
        Map<String, Integer> result = new LinkedHashMap<>();
        for (Object[] row : raw) {
            result.put((String) row[0], ((Long) row[1]).intValue());
        }
        return result;
    }

    @Override
    public List<Object> getChiTietKhoaHoc() {
        return new ArrayList<>(khoaHocRepository.chiTietKhoaHoc());
    }

    @Override
    public double tongTienNenTang() {
        Double result = thuNhapNenTangRepository.tongTienNenTang();
        return result != null ? result : 0.0;
    }

    @Override
    public int tongNhanVien() {
        return taiKhoanRepository.countNhanVien();
    }
}