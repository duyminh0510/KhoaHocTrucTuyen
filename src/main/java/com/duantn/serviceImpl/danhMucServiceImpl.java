package com.duantn.serviceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.duantn.entities.DanhMuc;
import com.duantn.repositories.DanhMucRepository;
import com.duantn.services.DanhMucService;

@Service
public class DanhMucServiceImpl implements DanhMucService {

    @Autowired
    private DanhMucRepository danhMucRepository;

    @Override
    public List<DanhMuc> layTatCa() {
        return danhMucRepository.findAll();
    }

    @Override
    public DanhMuc capNhat(Integer id, DanhMuc danhMuc) {
        return danhMucRepository.save(danhMuc);
    }

    @Override
    public DanhMuc layTheoId(Integer id) {
        Optional<DanhMuc> optional = danhMucRepository.findById(id);
        return optional.orElse(null);
    }

    @Override
    public boolean daTonTaiTen(String tenDanhMuc) {
        return danhMucRepository.existsByTenDanhMucIgnoreCase(tenDanhMuc);
    }

    @Override
    public boolean daTonTaiTenKhacId(String tenDanhMuc, Integer idHienTai) {
        DanhMuc dm = danhMucRepository.findByTenDanhMucIgnoreCase(tenDanhMuc);
        return dm != null && !dm.getDanhmucId().equals(idHienTai);
    }

    @Override
    public void voHieuHoa(Integer id) {
        DanhMuc danhMuc = layTheoId(id);
        danhMuc.setIsDeleted(true);
        danhMucRepository.save(danhMuc);
    }

    @Override
    public void kichHoat(Integer id) {
        DanhMuc danhMuc = layTheoId(id);
        danhMuc.setIsDeleted(false);
        danhMucRepository.save(danhMuc);
    }

    @Override
    public Optional<DanhMuc> findBySlug(String slug) {
        return danhMucRepository.findBySlug(slug); // nhớ viết method này trong repository nếu chưa có
    }

    @Override
    public DanhMuc taoDanhMuc(DanhMuc danhMuc) {
        return danhMucRepository.save(danhMuc);
    }
}