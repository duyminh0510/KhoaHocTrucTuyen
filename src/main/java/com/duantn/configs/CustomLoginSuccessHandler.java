package com.duantn.configs;

import java.io.IOException;
import java.util.Collection;

import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.duantn.services.GiangVienService;
import com.duantn.services.TaiKhoanService;

import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.DefaultRedirectStrategy;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Autowired
    private TaiKhoanService taiKhoanService;

    @Autowired
    private GiangVienService giangVienService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication)
            throws IOException, ServletException {

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String redirectUrl = null;

        // 🟢 Lấy email đăng nhập
        String email = authentication.getName(); // hoặc ((UserDetails)authentication.getPrincipal()).getUsername()

        // 🟢 Lấy đối tượng TaiKhoan từ email
        var taiKhoan = taiKhoanService.findByEmail(email);

        // 🟢 Lưu vào session
        request.getSession().setAttribute("user", taiKhoan);


        for (GrantedAuthority authority : authorities) {
            String role = authority.getAuthority();

            if ("ROLE_GIANGVIEN".equals(role)) {
                var giangVien = giangVienService.getByTaiKhoan(taiKhoan);
                request.getSession().setAttribute("giangVien", giangVien);
            }

            switch (role) {
                case "ROLE_ADMIN":
                    redirectUrl = "/admin";
                    break;

                case "ROLE_NHANVIEN":
                    redirectUrl = "/nhanvien";
                    break;

                case "ROLE_HOCVIEN":
                case "ROLE_GIANGVIEN":
                    // Kiểm tra nếu có URL trước đó người dùng cố truy cập
                    SavedRequest savedRequest = new HttpSessionRequestCache().getRequest(request, response);
                    if (savedRequest != null) {
                        String targetUrl = savedRequest.getRedirectUrl();
                        // Tránh redirect về trang admin/nhanvien nếu không đủ quyền
                        if (!targetUrl.contains("/admin") && !targetUrl.contains("/nhanvien")) {
                            redirectUrl = targetUrl;
                            break;
                        }
                    }
                    // Nếu không có URL trước hoặc bị cấm → về /
                    redirectUrl = "/";
                    break;

                default:
                    redirectUrl = "/"; // fallback
            }

            if (redirectUrl != null) {
                break;
            }
        }

        if (redirectUrl == null) {
            redirectUrl = "/";
        }

        redirectStrategy.sendRedirect(request, response, redirectUrl);
    }

}
