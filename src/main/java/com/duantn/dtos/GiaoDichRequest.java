package com.duantn.dtos;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class GiaoDichRequest {
    private BigDecimal tongTien;
    private List<Integer> khoaHocIds;
}
