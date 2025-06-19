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

        SavedRequest savedRequest = new HttpSessionRequestCache().getRequest(request, response);

        if (savedRequest != null) {
            // Redirect to the originally requested URL (e.g. /gio-hang)
            String targetUrl = savedRequest.getRedirectUrl();
            redirectStrategy.sendRedirect(request, response, targetUrl);
            return;
        }

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        String redirectUrl = "/"; // trang mặc định

        for (GrantedAuthority authority : authorities) {
            String role = authority.getAuthority();

            if (role.equals("ROLE_ADMIN")) {
                redirectUrl = "/admin";
                break;
            } else if (role.equals("ROLE_GIANGVIEN")) {
                redirectUrl = "/giangvien";
                break;
            } else if (role.equals("ROLE_HOCVIEN")) {
                redirectUrl = "/hocvien";
                break;
            } else if (role.equals("ROLE_NHANVIEN")) {
                redirectUrl = "/nhanvien";
                break;
            }
            System.out.println("Đăng nhập với quyền: " + role);
        }

        response.sendRedirect(redirectUrl);
    }
}