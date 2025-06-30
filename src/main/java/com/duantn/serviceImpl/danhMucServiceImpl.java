package com.duantn.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.duantn.entities.DanhMuc;
import com.duantn.repositories.DanhMucRepository;
import com.duantn.services.DanhMucService;

@Service
public class danhMucServiceImpl implements DanhMucService {
    @Autowired
    private DanhMucRepository repository;

    @Override
    public DanhMuc taoDanhMuc(DanhMuc danhMuc) {
        if (repository.existsByTenDanhMuc(danhMuc.getTenDanhMuc())) {
            throw new IllegalArgumentException("Danh mục đã tồn tại!");
        }
        return repository.save(danhMuc);
    }

    @Override
    public DanhMuc layTheoId(Integer id) {
        return repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Không tìm thấy DanhMuc với id " + id));
    }

    @Override
    public List<DanhMuc> layTatCa() {
        return repository.findAll();
    }

    @Override
    public DanhMuc capNhat(Integer id, DanhMuc danhMuc) {
        DanhMuc existing = layTheoId(id);
        existing.setTenDanhMuc(danhMuc.getTenDanhMuc());
        return repository.save(existing);
    }

    @Override
    public boolean daTonTaiTen(String tenDanhMuc) {
        return repository.existsByTenDanhMucIgnoreCase(tenDanhMuc);
    }

    @Override
    public boolean daTonTaiTenKhacId(String tenDanhMuc, Integer idHienTai) {
        DanhMuc dm = repository.findByTenDanhMucIgnoreCase(tenDanhMuc);
        return dm != null && !dm.getDanhmucId().equals(idHienTai);
    }

    @Override
    public void voHieuHoa(Integer id) {
        DanhMuc danhMuc = layTheoId(id);
        danhMuc.setIsDeleted(false); // hoặc true nếu bạn dùng ngược
        repository.save(danhMuc);
    }

    @Override
    public void kichHoat(Integer id) {
        DanhMuc danhMuc = layTheoId(id);
        danhMuc.setIsDeleted(true); // bật lại
        repository.save(danhMuc);
    }

}
