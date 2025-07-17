package com.duantn.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface DoanhThuGiangVienService {
    BigDecimal tinhDoanhThuTheoKhoangNgay(Integer taiKhoanId, LocalDateTime startDate, LocalDateTime endDate);

}
