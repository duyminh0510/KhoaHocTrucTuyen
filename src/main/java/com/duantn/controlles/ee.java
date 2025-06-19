package com.duantn.controlles;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ee {
    @GetMapping("/e")
    public String home() {
        return "test"; // trả về view tên home.html trong templates
    }

}