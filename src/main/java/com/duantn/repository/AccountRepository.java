package com.duantn.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.duantn.entitys.Account;

public interface AccountRepository extends JpaRepository<Account, Integer> {
<<<<<<< HEAD
    Optional<Account> findByEmail(String email);
=======
    Optional<Account> findByEmailAndPasswordAndStatusTrue(String email, String password);

    Optional<Account> findByEmailAndStatusTrue(String email);

    boolean existsByEmail(String email);

>>>>>>> 6167dc0c9113ce285e6d0f0142867a23dbe71d02
}
