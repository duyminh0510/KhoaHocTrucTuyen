package com.duantn.configs;

import java.io.IOException;
import java.util.Collection;

import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.DefaultRedirectStrategy;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication)
            throws IOException, ServletException {

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        for (GrantedAuthority authority : authorities) {
            String role = authority.getAuthority();

            // Nếu là ADMIN
            if (role.equals("ROLE_ADMIN")) {
                redirectStrategy.sendRedirect(request, response, "/admin");
                return;
            }

            // Nếu là NHÂN VIÊN
            if (role.equals("ROLE_NHANVIEN")) {
                redirectStrategy.sendRedirect(request, response, "/nhanvien");
                return;
            }

            // Nếu là HỌC VIÊN hoặc GIẢNG VIÊN
            if (role.equals("ROLE_HOCVIEN") || role.equals("ROLE_GIANGVIEN")) {
                // Ưu tiên quay lại trang đã cố truy cập trước đó
                SavedRequest savedRequest = new HttpSessionRequestCache().getRequest(request, response);
                if (savedRequest != null) {
                    String targetUrl = savedRequest.getRedirectUrl();
                    redirectStrategy.sendRedirect(request, response, targetUrl);
                } else {
                    // Nếu không có trang trước, chuyển về /
                    redirectStrategy.sendRedirect(request, response, "/");
                }
                return;
            }
        }

        // Mặc định
        redirectStrategy.sendRedirect(request, response, "/");
    }
}
