package com.duantn.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.duantn.entitys.VerificationToken;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    Optional<VerificationToken> findByToken(String token);

    Optional<VerificationToken> findByEmail(String email);
}
