package com.duantn.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class GiangVienRegistrationDto {

    @NotBlank(message = "Vui lòng nhập họ tên")
    private String hoTen;

    @NotBlank(message = "Vui lòng nhập số điện thoại")
    private String sdt;

    @Email(message = "Email không hợp lệ")
    @NotBlank(message = "Vui lòng nhập email")
    private String email;

    @NotBlank(message = "Vui lòng nhập mật khẩu")
    @Size(min = 6, message = "Mật khẩu phải có ít nhất 6 ký tự")
    private String password;

    @NotBlank(message = "Vui lòng nhập kỹ năng")
    private String kyNang;

    @NotBlank(message = "Vui lòng nhập kinh nghiệm")
    private String kinhNghiem;

    @NotBlank(message = "Vui lòng nhập CCCD")
    private String CCCD;
}
