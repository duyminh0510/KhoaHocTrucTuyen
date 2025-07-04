package com.duantn.services;

import com.duantn.entities.KhoaHoc;
import com.duantn.entities.NguoiDungThichKhoaHoc;
import com.duantn.entities.TaiKhoan;
import com.duantn.enums.TrangThaiGiaoDich;
import com.duantn.enums.TrangThaiKhoaHoc;
import com.duantn.repositories.KhoaHocRepository;
import com.duantn.repositories.NguoiDungThichKhoaHocRepository;
import com.duantn.repositories.TaiKhoanRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class KhoaHocService {

    @Autowired
    private KhoaHocRepository khoaHocRepository;
    @Autowired
    private TaiKhoanRepository taiKhoanRepository;
    @Autowired
    private NguoiDungThichKhoaHocRepository nguoiDungThichKhoaHocRepository;

    public List<KhoaHoc> getTatCaKhoaHoc() {
        return khoaHocRepository.findAll();
    }

    public KhoaHoc getKhoaHocById(Integer id) {
        return khoaHocRepository.findById(id).orElse(null);
    }

    public List<KhoaHoc> layTatCaKhoaHocCanDuyet() {
        return khoaHocRepository.findAllByTrangThai(TrangThaiKhoaHoc.PENDING_APPROVAL);
    }

    public List<KhoaHoc> getNewestCourses(int count) {
        Pageable pageable = PageRequest.of(0, count);
        return khoaHocRepository.findByOrderByNgayTaoDesc(pageable);
    }

    public List<KhoaHoc> getTopPurchasedCourses(int count) {
        Pageable pageable = PageRequest.of(0, count);
        List<Integer> topIds = khoaHocRepository.findTopPurchasedCourseIds(pageable);

        if (topIds.isEmpty())
            return Collections.emptyList();

        List<KhoaHoc> courses = khoaHocRepository.findByIdInWithDetails(topIds);

        return courses.stream()
                .sorted(Comparator.comparing(course -> topIds.indexOf(course.getKhoahocId())))
                .toList();
    }

    public List<KhoaHoc> getEnrolledCourses(String email) {
        return khoaHocRepository.findEnrolledCoursesByEmail(email, TrangThaiGiaoDich.HOAN_THANH);
    }

    @Transactional
    public boolean toggleLike(Integer khoahocId, Integer taikhoanId) {
        KhoaHoc khoaHoc = khoaHocRepository.findById(khoahocId).orElse(null);
        TaiKhoan taiKhoan = taiKhoanRepository.findById(taikhoanId).orElse(null);

        if (khoaHoc == null || taiKhoan == null)
            return false;

        Optional<NguoiDungThichKhoaHoc> like = nguoiDungThichKhoaHocRepository
                .findByTaiKhoan_TaikhoanIdAndKhoaHoc_KhoahocId(taikhoanId, khoahocId);

        if (like.isPresent()) {
            nguoiDungThichKhoaHocRepository.delete(like.get());
            khoaHoc.setLuotThich(Math.max((khoaHoc.getLuotThich() != null ? khoaHoc.getLuotThich() : 0) - 1, 0));
            khoaHocRepository.save(khoaHoc);
            return false;
        } else {
            NguoiDungThichKhoaHoc newLike = new NguoiDungThichKhoaHoc(null, taiKhoan, khoaHoc);
            nguoiDungThichKhoaHocRepository.save(newLike);
            khoaHoc.setLuotThich((khoaHoc.getLuotThich() != null ? khoaHoc.getLuotThich() : 0) + 1);
            khoaHocRepository.save(khoaHoc);
            return true;
        }
    }

    public List<KhoaHoc> findLikedCoursesByAccountId(Integer currentUserId) {
        if (currentUserId == null)
            return Collections.emptyList();
        return khoaHocRepository.findLikedCoursesByAccountId(currentUserId);
    }

    @Transactional(readOnly = true)
    public KhoaHoc getKhoaHocByIdWithDetails(Integer id) {
        return khoaHocRepository.findByIdWithDetails(id).orElse(null);
    }

    public Optional<KhoaHoc> findById(Integer id) {
        return khoaHocRepository.findById(id);
    }
}
