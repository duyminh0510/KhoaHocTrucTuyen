package com.duantn.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.duantn.entities.TaiKhoan;
import com.duantn.repositories.TaiKhoanRepository;
import com.duantn.services.TaiKhoanService;

@Service
public class TaiKhoanServiceImpl implements TaiKhoanService {
    @Autowired
    private TaiKhoanRepository taiKhoanRepo;

    @Override
    public TaiKhoan login(String email, String password) {
        return taiKhoanRepo.findByEmailAndPassword(email, password);
    }

    @Override
    public TaiKhoan register(TaiKhoan account) {
        return taiKhoanRepo.save(account);
    }

    @Override
    public TaiKhoan findByUsername(String username) {
        return taiKhoanRepo.findByEmail(username).orElse(null);
    }
}
