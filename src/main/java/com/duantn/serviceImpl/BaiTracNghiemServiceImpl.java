package com.duantn.serviceImpl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.duantn.entities.BaiGiang;
import com.duantn.entities.BaiTracNghiem;
import com.duantn.entities.CauHoi;
import com.duantn.entities.DapAn;
import com.duantn.repositories.BaiTracNghiemRepository;
import com.duantn.services.BaiTracNghiemService;
import com.duantn.services.CauHoiService;
import com.duantn.services.DapAnService;

@Service
public class BaiTracNghiemServiceImpl implements BaiTracNghiemService {

    @Autowired
    BaiTracNghiemRepository baiTracNghiemRepository;

    @Autowired
    private CauHoiService cauHoiService;

    @Autowired
    private DapAnService dapAnService;

    @Override
    public BaiTracNghiem save(BaiTracNghiem tracNghiem) {
        return baiTracNghiemRepository.save(tracNghiem);
    }

    @Override
    public BaiTracNghiem findByBaiGiangId(Integer baiGiangId) {
        return baiTracNghiemRepository.findByBaiGiang_BaiGiangId(baiGiangId);
    }

    @Override
    public void saveBaiTracNghiemVaCauHoi(BaiTracNghiem tracMoi, BaiGiang baiGiang) {

        // Kiểm tra nếu đã có video hoặc bài viết
        if (baiGiang.getVideoBaiGiang() != null || baiGiang.getBaiViet() != null) {
            throw new IllegalStateException(
                    "Bài giảng đã có nội dung khác (video hoặc bài viết). Vui lòng xóa trước khi thêm trắc nghiệm.");
        }

        // Gán bài giảng cho bài trắc nghiệm
        tracMoi.setBaiGiang(baiGiang);

        // Kiểm tra đã tồn tại bài trắc nghiệm chưa
        BaiTracNghiem tracCu = findByBaiGiangId(baiGiang.getBaiGiangId());
        BaiTracNghiem tracLuu;

        if (tracCu != null) {
            tracCu.setTenbai(tracMoi.getTenbai());
            tracCu.setTrangthai(tracMoi.getTrangthai());
            tracLuu = save(tracCu);
        } else {
            tracLuu = save(tracMoi);
        }

        // Xử lý câu hỏi
        if (tracMoi.getCauHoiList() != null) {
            List<CauHoi> cauHoiCuList = cauHoiService.findByBaiTracNghiemId(tracLuu.getTracnghiemId());
            Map<Integer, CauHoi> cauHoiCuMap = cauHoiCuList.stream()
                    .filter(ch -> ch.getCauHoiId() != null)
                    .collect(Collectors.toMap(CauHoi::getCauHoiId, ch -> ch));

            Set<Integer> cauHoiMoiIds = new HashSet<>();
            int index = 0;

            for (CauHoi chMoi : tracMoi.getCauHoiList()) {
                chMoi.setBaiTracNghiem(tracLuu);
                chMoi.setCauHoiSo(++index);

                // 🔹 Bước 1: lưu câu hỏi trước để có cauHoiId
                CauHoi chLuu;
                if (chMoi.getCauHoiId() != null && cauHoiCuMap.containsKey(chMoi.getCauHoiId())) {
                    CauHoi chCu = cauHoiCuMap.get(chMoi.getCauHoiId());
                    chCu.setTenCauHoi(chMoi.getTenCauHoi());
                    chCu.setCauHoiSo(chMoi.getCauHoiSo());
                    chCu.setTrangthai(chMoi.getTrangthai());
                    chLuu = cauHoiService.save(chCu);
                    cauHoiMoiIds.add(chCu.getCauHoiId());

                } else {
                    chLuu = cauHoiService.saveAndFlush(chMoi); // 💡 Save and flush để đảm bảo có ID ngay
                    cauHoiMoiIds.add(chLuu.getCauHoiId()); // Thêm vào danh sách để khỏi bị xóa

                }

                // 🔸 Bước 2: xử lý đáp án
                List<DapAn> dapAnFormList = chMoi.getDapAnList();
                int dapAnDungIndex = chMoi.getDapAnDungIndex();

                if (dapAnFormList != null && dapAnFormList.size() == 4) {
                    List<DapAn> dapAnToSave = new ArrayList<>();

                    for (int i = 0; i < 4; i++) {
                        DapAn dapAnForm = dapAnFormList.get(i);

                        DapAn da = new DapAn();
                        da.setThuTuDapAn(i + 1);
                        da.setNoiDungDapAn(dapAnForm.getNoiDungDapAn());
                        da.setDapAnDung(i == dapAnDungIndex);
                        da.setGiaThichDapan(i == dapAnDungIndex ? chMoi.getGiaiThich() : null);
                        da.setTrangthai(true);
                        da.setCauHoi(chLuu); // ✅ chLuu lúc này đã có ID

                        dapAnToSave.add(da);
                    }

                    // 🔹 Xoá đáp án cũ của câu hỏi này trước nếu cần
                    dapAnService.deleteByCauHoiId(chLuu.getCauHoiId());

                    // 🔹 Sau đó mới lưu đáp án mới
                    dapAnService.saveAll(dapAnToSave);
                }
            }

            // Xoá câu hỏi cũ nếu không còn trong danh sách
            for (CauHoi chCu : cauHoiCuList) {
                if (!cauHoiMoiIds.contains(chCu.getCauHoiId())) {
                    cauHoiService.deleteById(chCu.getCauHoiId());
                }
            }
        }
    }

}
