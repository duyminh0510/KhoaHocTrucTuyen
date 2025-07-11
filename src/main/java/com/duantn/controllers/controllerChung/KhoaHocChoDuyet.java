package com.duantn.controllers.controllerChung;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.duantn.entities.DanhMuc;
import com.duantn.entities.KhoaHoc;
import com.duantn.enums.TrangThaiKhoaHoc;
import com.duantn.services.KhoaHocService;

@Controller
public class KhoaHocChoDuyet {
    @Autowired
    private KhoaHocService khoaHocService;

    @GetMapping("/khoa-hoc-cho-phe-duyet")
    public String showPendingCourses(Model model) {
        List<DanhMuc> danhMucList = khoaHocService.getDanhMucCoKhoaHoc();
        Map<Integer, List<KhoaHoc>> khoaHocTheoDanhMuc = new HashMap<>();
        for (DanhMuc dm : danhMucList) {
            List<KhoaHoc> ds = khoaHocService.getKhoaHocTheoDanhMucAndTrangThai(dm.getDanhmucId(),
                    TrangThaiKhoaHoc.PENDING_APPROVAL);
            khoaHocTheoDanhMuc.put(dm.getDanhmucId(), ds);
        }
        model.addAttribute("danhMucList", danhMucList);
        model.addAttribute("khoaHocTheoDanhMuc", khoaHocTheoDanhMuc);
        return "views/gdienChung/khoaHocChoPheDuyet";
    }
}
