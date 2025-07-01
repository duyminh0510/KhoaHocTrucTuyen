package com.duantn.services;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.duantn.entities.TaiKhoan;
import com.duantn.repositories.TaiKhoanRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {
        @Autowired
        private TaiKhoanRepository accountRepo;

        @Override
        public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
                TaiKhoan account = accountRepo.findByEmail(email)
                                .orElseThrow(() -> new UsernameNotFoundException("Tài khoản không tồn tại"));

                String role = account.getRole().getName();
                System.out.println("Role được gán:" + role);
                System.out.println("Mật khẩu trong DB: " + account.getPassword());

                return new org.springframework.security.core.userdetails.User(
                                account.getEmail(),
                                account.getPassword(),
                                Collections.singletonList(
                                                new SimpleGrantedAuthority(account.getRole().getName())));

        }

}
