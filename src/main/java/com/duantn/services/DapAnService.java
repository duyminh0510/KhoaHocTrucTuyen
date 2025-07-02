package com.duantn.services;

import java.util.List;
import java.util.Optional;

import com.duantn.entities.DapAn;

public interface DapAnService {
    Optional<DapAn> findById(Integer id);

    DapAn save(DapAn dapan);

    void saveAll(List<DapAn> dapAns);

    void deleteByCauHoiId(Integer cauHoiId);

}
