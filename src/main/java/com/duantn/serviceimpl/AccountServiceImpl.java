package com.duantn.serviceimpl;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.duantn.entitys.Account;
import com.duantn.entitys.Role;
import com.duantn.repository.AccountRepository;
import com.duantn.repository.RoleRepository;
import com.duantn.services.AccountService;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Account login(String email, String password) {
        return accountRepository.findByEmailAndPasswordAndStatusTrue(email, password).orElse(null);
    }

    @Override
    public Account register(Account account) {
        Optional<Account> exist = accountRepository.findByEmailAndStatusTrue(account.getEmail());
        if (exist.isPresent()) {
            return null; // Email đã tồn tại
        }

        // Gán role mặc định ROLE_USER (ID = 1)
        Role defaultRole = roleRepository.findById(1)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy role mặc định"));
        account.setRole(defaultRole);

        account.setStatus(true);
        return accountRepository.save(account);
    }
}
