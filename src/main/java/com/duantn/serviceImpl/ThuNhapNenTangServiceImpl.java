package com.duantn.serviceImpl;

import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.duantn.repositories.ThuNhapNenTangRepository;
import com.duantn.services.ThuNhapNenTangService;

@Service
public class ThuNhapNenTangServiceImpl implements ThuNhapNenTangService {

    @Autowired
    private ThuNhapNenTangRepository thuNhapNenTangRepository;

    @Override
    public BigDecimal layTongThuNhap() {
        return thuNhapNenTangRepository.getTongThuNhap();
    }
}
