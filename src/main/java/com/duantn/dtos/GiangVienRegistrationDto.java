package com.duantn.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class GiangVienRegistrationDto {
    @NotBlank(message = "Vui lòng nhập kỹ năng")
    private String kyNang;

    @NotBlank(message = "Vui lòng nhập kinh nghiệm")
    private String kinhNghiem;

    @NotBlank(message = "Vui lòng nhập CCCD")
    private String CCCD;
}
