package com.duantn.services;

import com.duantn.entities.TaiKhoan;

public interface TaiKhoanService<T extends TaiKhoan> {
    TaiKhoan login(String email, String password);

    TaiKhoan register(TaiKhoan taiKhoan);
}
