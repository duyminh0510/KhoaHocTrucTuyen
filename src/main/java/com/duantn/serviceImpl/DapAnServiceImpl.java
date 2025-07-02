package com.duantn.serviceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.duantn.entities.DapAn;
import com.duantn.repositories.DapAnRepository;
import com.duantn.services.DapAnService;

@Service
public class DapAnServiceImpl implements DapAnService {
    @Autowired
    DapAnRepository dapAnRepository;

    @Override
    public Optional<DapAn> findById(Integer id) {
        return dapAnRepository.findById(id);
    }

    @Override
    public DapAn save(DapAn da) {
        return dapAnRepository.save(da);
    }

    @Override
    public void saveAll(List<DapAn> dapAns) {
        dapAnRepository.saveAll(dapAns);
    }

    @Override
    public void deleteByCauHoiId(Integer cauHoiId) {
        dapAnRepository.deleteByCauHoiId(cauHoiId);
    }

}
