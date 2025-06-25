package com.duantn.services;

import com.duantn.entities.GiaoDichKhoaHoc;
import com.duantn.entities.GiaoDichKhoaHocChiTiet;
import com.duantn.repositories.GiaoDichKhoaHocChiTietRepository;
import com.duantn.repositories.GiaoDichKhoaHocRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GiaoDichService {
    
    @Autowired
    private GiaoDichKhoaHocChiTietRepository giaoDichKhoaHocChiTietRepository;

    @Autowired
    private GiaoDichKhoaHocRepository giaoDichKhoaHocRepository;
    
    // Lấy tất cả chi tiết giao dịch (cách cũ)
    public List<GiaoDichKhoaHocChiTiet> getAllGiaoDichChiTiet() {
        return giaoDichKhoaHocChiTietRepository.findAllWithDetails();
    }
    
    // Lấy tất cả giao dịch từ bảng chính (cách mới)
    public List<GiaoDichKhoaHoc> getAllGiaoDich() {
        return giaoDichKhoaHocRepository.findAll(Sort.by(Sort.Direction.DESC, "ngayGiaoDich"));
    }

    public List<GiaoDichKhoaHocChiTiet> getLichSuGiaoDichByAccountId(Integer accountId) {
        return giaoDichKhoaHocChiTietRepository.findByAccountId(accountId);
    }
} 