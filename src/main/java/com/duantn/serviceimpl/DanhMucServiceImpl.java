package com.duantn.serviceimpl;

import java.util.List;
import org.springframework.stereotype.Service;
import com.duantn.entitys.DanhMuc;
import com.duantn.repository.DanhMucRepository;
import com.duantn.services.DanhMucService;

@Service
public class DanhMucServiceImpl implements DanhMucService {

    private final DanhMucRepository danhMucRepository;

    public DanhMucServiceImpl(DanhMucRepository danhMucRepository) {
        this.danhMucRepository = danhMucRepository;
    }

    @Override
    public List<DanhMuc> findAll() {
        return danhMucRepository.findAll();
    }

    @Override
    public DanhMuc save(DanhMuc danhMuc) {
        return danhMucRepository.save(danhMuc);
    }

    @Override
    public void deleteById(Integer id) {
        danhMucRepository.deleteById(id);
    }

    @Override
    public DanhMuc findById(Integer id) {
        return danhMucRepository.findById(id).orElse(null);
    }
}
