package com.duantn.controllers.controllerHocVien;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class KetQuaController {
    @RequestMapping("/ket-qua-lam-bai")
    public String requestMethodName() {
        return "views/gdienHocVien/ket-qua";
    }

}
