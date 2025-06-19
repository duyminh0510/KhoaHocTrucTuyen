package com.duantn.controllers.Admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CategoryController {

    @GetMapping("/admin/category")
    public String adminIndex() {
        return "views/Admin/DanhMuc"; // -> sẽ tìm file:
                                      // src/main/resources/templates/views/Admin/DanhMuc.html
    }
}
