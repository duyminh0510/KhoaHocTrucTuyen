package com.duantn.controlles.controllechung;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class HomeController {
    @GetMapping("/")
    public String kinhTePage(HttpServletRequest request, Model model) {
        model.addAttribute("currentUri", request.getRequestURI());
        return "views/gdienChung/home";
    }

}
