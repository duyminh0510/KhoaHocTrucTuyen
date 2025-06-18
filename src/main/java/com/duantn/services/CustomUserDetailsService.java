package com.duantn.services;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.duantn.entitys.Account;
import com.duantn.repository.AccountRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {
        @Autowired
        private AccountRepository accountRepo;

        @Override
        public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
                Account account = accountRepo.findByEmail(email)
                                .orElseThrow(() -> new UsernameNotFoundException("Tài khoản không tồn tại"));

                String role = account.getRole().getName();
                System.out.println("Role được gán: ROLE_" + role);
                System.out.println("Mật khẩu trong DB: " + account.getPassword());

                return new org.springframework.security.core.userdetails.User(
                                account.getEmail(),
                                account.getPassword(),
                                Collections.singletonList(
                                                new SimpleGrantedAuthority("ROLE_" + account.getRole().getName())));

        }

}
