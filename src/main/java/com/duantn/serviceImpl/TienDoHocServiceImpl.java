package com.duantn.serviceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.duantn.entities.BaiGiang;
import com.duantn.entities.Chuong;
import com.duantn.entities.DangHoc;
import com.duantn.entities.KhoaHoc;
import com.duantn.entities.TienDoHoc;
import com.duantn.repositories.TienDoHocRepository;
import com.duantn.services.BaiGiangService;
import com.duantn.services.ChuongService;
import com.duantn.services.DangHocService;
import com.duantn.services.TienDoHocService;

import jakarta.transaction.Transactional;

@Service
public class TienDoHocServiceImpl implements TienDoHocService {

    @Autowired
    private TienDoHocRepository tienDoHocRepository;

    @Autowired
    private ChuongService chuongService;

    @Autowired
    private BaiGiangService baiGiangService;

    @Autowired
    private DangHocService dangHocService;

    @Override
    public List<TienDoHoc> findByDangHocId(Integer dangHocId) {
        return tienDoHocRepository.findByDangHoc_DanghocId(dangHocId);
    }

    @Override
    @Transactional
    public void taoTienDoChoDangHoc(DangHoc dangHoc) {
        KhoaHoc khoaHoc = dangHoc.getKhoahoc();
        if (khoaHoc == null)
            return;

        // Lấy danh sách chương từ service để đảm bảo đầy đủ bài giảng (tránh lazy load
        // lỗi)
        List<Chuong> chuongs = chuongService.findByKhoaHocId(khoaHoc.getKhoahocId());
        List<TienDoHoc> danhSachTienDo = new ArrayList<>();

        for (Chuong chuong : chuongs) {
            List<BaiGiang> baiGiangs = chuong.getBaiGiangs();
            if (baiGiangs == null)
                continue;

            for (BaiGiang baiGiang : baiGiangs) {
                TienDoHoc tienDo = TienDoHoc.builder()
                        .dangHoc(dangHoc)
                        .baiGiang(baiGiang)
                        .trangthai(false)
                        .tenHocVien(dangHoc.getTaikhoan().getName())
                        .tenKhoaHoc(khoaHoc.getTenKhoaHoc())
                        .build();
                danhSachTienDo.add(tienDo);
            }
        }

        tienDoHocRepository.saveAll(danhSachTienDo);
    }

    @Override
    public void capNhatTienDoSauKhiHoc(Integer taiKhoanId, Integer baiGiangId) {
        BaiGiang baiGiang = baiGiangService.findBaiGiangById(baiGiangId);
        if (baiGiang == null || baiGiang.getChuong() == null)
            return;

        Integer khoaHocId = baiGiang.getChuong().getKhoahoc().getKhoahocId();
        DangHoc dangHoc = dangHocService.findByTaiKhoanIdAndKhoaHocId(taiKhoanId, khoaHocId);
        if (dangHoc == null)
            return;

        TienDoHoc tienDo = tienDoHocRepository.findByDangHoc_DanghocIdAndBaiGiang_BaiGiangId(dangHoc.getDanghocId(),
                baiGiangId);
        if (tienDo == null) {
            tienDo = TienDoHoc.builder()
                    .dangHoc(dangHoc)
                    .baiGiang(baiGiang)
                    .trangthai(true)
                    .ngayhoanthanh(LocalDateTime.now())
                    .tenHocVien(dangHoc.getTaikhoan().getName())
                    .tenKhoaHoc(dangHoc.getKhoahoc().getTenKhoaHoc())
                    .build();
        } else if (!tienDo.isTrangthai()) {
            tienDo.setTrangthai(true);
            tienDo.setNgayhoanthanh(LocalDateTime.now());
        }

        tienDoHocRepository.save(tienDo);
    }

}
