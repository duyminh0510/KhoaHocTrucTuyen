package com.duantn.controllers.controllerGiangVien;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class XemDanhGiaController {

    @RequestMapping("/giangvien/danh-gia-tu-khoa-hoc")
    public String requestMethodName() {
        return "views/gdienGiangVien/quan-ly-danh-gia";
    }

}
