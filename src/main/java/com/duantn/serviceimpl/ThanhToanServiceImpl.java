package com.duantn.serviceimpl;

import com.duantn.entitys.ThanhToan;
import com.duantn.repository.ThanhToanRepository;
import com.duantn.services.ThanhToanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ThanhToanServiceImpl implements ThanhToanService {
    @Autowired
    private ThanhToanRepository repo;

    @Override
    public List<ThanhToan> findAll() {
        return repo.findAll();
    }

    @Override
    public ThanhToan findById(Integer id) {
        return repo.findById(id).orElse(null);
    }

    @Override
    public ThanhToan save(ThanhToan thanhToan) {
        return repo.save(thanhToan);
    }

    @Override
    public void deleteById(Integer id) {
        repo.deleteById(id);
    }
} 