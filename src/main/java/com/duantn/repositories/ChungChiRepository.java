package com.duantn.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.duantn.entities.ChungChi;

public interface ChungChiRepository extends JpaRepository <ChungChi, Integer> {
    // Additional query methods can be defined here if needed

}
