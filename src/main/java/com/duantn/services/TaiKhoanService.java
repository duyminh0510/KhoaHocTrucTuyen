package com.duantn.services;

import com.duantn.entitys.TaiKhoan;

public interface TaiKhoanService {
    TaiKhoan login(String email, String password);

    TaiKhoan register(TaiKhoan account);
}
