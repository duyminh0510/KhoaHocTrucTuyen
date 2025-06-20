package com.duantn.controllers.controllerChung;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TrangChuController {
    @ModelAttribute("currentUri")
    public String getCurrentUri(HttpServletRequest request) {
        String uri = request.getRequestURI();
        return (uri == null || uri.isEmpty() || "/".equals(uri)) ? "/" : uri;
    }

    @RequestMapping("/")
    public String requestMethodName() {
        return "views/gdienChung/home";
    }

}
