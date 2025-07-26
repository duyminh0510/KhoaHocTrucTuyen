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
import com.duantn.repositories.DangHocRepository;

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

    @Autowired
    private DangHocRepository dangHocRepository;

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
                if (baiGiang.getTrangthai() == null || !baiGiang.getTrangthai()) continue; // chỉ tạo tiến độ cho bài giảng hoạt động
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
        System.out.println("[Service] capNhatTienDoSauKhiHoc: taiKhoanId=" + taiKhoanId + ", baiGiangId=" + baiGiangId);
        BaiGiang baiGiang = baiGiangService.findBaiGiangById(baiGiangId);
        if (baiGiang == null || baiGiang.getChuong() == null) {
            System.out.println("[Service] Không tìm thấy bài giảng hoặc chương!");
            return;
        }
        Integer khoaHocId = baiGiang.getChuong().getKhoahoc().getKhoahocId();
        DangHoc dangHoc = dangHocService.findByTaiKhoanIdAndKhoaHocId(taiKhoanId, khoaHocId);
        if (dangHoc == null) {
            System.out.println("[Service] Không tìm thấy DangHoc!");
            return;
        }
        TienDoHoc tienDo = tienDoHocRepository.findByDangHoc_DanghocIdAndBaiGiang_BaiGiangId(dangHoc.getDanghocId(), baiGiangId);
        if (tienDo == null) {
            System.out.println("[Service] Chưa có bản ghi TienDoHoc, tạo mới!");
            tienDo = TienDoHoc.builder()
                    .dangHoc(dangHoc)
                    .baiGiang(baiGiang)
                    .trangthai(true)
                    .ngayhoanthanh(LocalDateTime.now())
                    .tenHocVien(dangHoc.getTaikhoan().getName())
                    .tenKhoaHoc(dangHoc.getKhoahoc().getTenKhoaHoc())
                    .build();
        } else if (!tienDo.isTrangthai()) {
            System.out.println("[Service] Đã có bản ghi, cập nhật trạng thái sang true!");
            tienDo.setTrangthai(true);
            tienDo.setNgayhoanthanh(LocalDateTime.now());
        } else {
            System.out.println("[Service] Bản ghi đã có trạng thái true, không cần cập nhật!");
        }
        tienDoHocRepository.save(tienDo);
        System.out.println("[Service] Đã lưu tiến độ học: tiendoId=" + tienDo.getTiendoId() + ", trạng thái=" + tienDo.isTrangthai());

        // Nếu tất cả các bài giảng đều đã hoàn thành, cập nhật trạng thái DangHoc
        List<TienDoHoc> allTienDo = tienDoHocRepository.findByDangHoc_DanghocId(dangHoc.getDanghocId());
        boolean allDone = allTienDo.stream().allMatch(TienDoHoc::isTrangthai);
        if (allDone && !dangHoc.isTrangthai()) {
            dangHoc.setTrangthai(true);
            dangHocRepository.save(dangHoc);
            System.out.println("[Service] Đã cập nhật DangHoc.trangthai = true vì đã hoàn thành 100%!");
            dangHocService.capNhatTrangThaiVaTaoChungChi(dangHoc); 
        }
    }

}
