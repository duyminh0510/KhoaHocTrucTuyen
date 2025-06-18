package com.duantn.configs;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.duantn.entitys.Account;
import com.duantn.entitys.Role;
import com.duantn.repository.AccountRepository;
import com.duantn.repository.RoleRepository;

@Component
@RequiredArgsConstructor
public class DataInitializer {

    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    private void ensureRoleExists(String roleName) {
        roleRepository.findByName(roleName).orElseGet(() -> {
            Role role = Role.builder().name(roleName).build();
            return roleRepository.save(role);
        });
    }

    @PostConstruct
    @Transactional
    public void init() {
        String superAdminEmail = "ngoctthpd10040@gmail.com";

        // Tạo các vai trò cần thiết
        ensureRoleExists("ROLE_ADMIN");
        ensureRoleExists("ROLE_INSTRUCTOR");
        ensureRoleExists("ROLE_STUDENT");
        ensureRoleExists("ROLE_EMPLOYEE");

        if (accountRepository.existsByEmail(superAdminEmail)) {
            return; // Đã tồn tại Super Admin
        }

        Role adminRole = roleRepository.findByName("ROLE_ADMIN").orElseThrow();

        Account superAdminUser = Account.builder()
                .name("Super Admin")
                .email(superAdminEmail)
                .password(passwordEncoder.encode("superpassword"))
                .role(adminRole)
                .build();

        accountRepository.saveAndFlush(superAdminUser);

        System.out.println("✅ Super Admin đã được khởi tạo thành công!");
    }
}