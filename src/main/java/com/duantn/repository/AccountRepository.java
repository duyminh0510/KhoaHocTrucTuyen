package com.duantn.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.duantn.entitys.Account;

public interface AccountRepository extends JpaRepository<Account, Integer> {
    Optional<Account> findByEmail(String email);

    boolean existsByEmail(String email);
}
