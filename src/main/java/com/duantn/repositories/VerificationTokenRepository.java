package com.duantn.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.duantn.entities.VerificationToken;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    Optional<VerificationToken> findByToken(String token);

    Optional<VerificationToken> findByEmail(String email);
}
