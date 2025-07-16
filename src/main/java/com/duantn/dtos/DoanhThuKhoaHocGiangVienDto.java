package com.duantn.dtos;

import java.math.BigDecimal;

public class DoanhThuKhoaHocGiangVienDto {
    private String tenKhoaHoc;
    private Long soLuotDangKy;
    private Double tongTienGiaoDich;
    private Double tongTienNhan;

    public DoanhThuKhoaHocGiangVienDto(String tenKhoaHoc, Long soLuotDangKy, BigDecimal tongTienGiaoDich,
            BigDecimal tongTienNhan) {
        this.tenKhoaHoc = tenKhoaHoc;
        this.soLuotDangKy = soLuotDangKy;
        this.tongTienGiaoDich = tongTienGiaoDich != null ? tongTienGiaoDich.doubleValue() : 0.0;
        this.tongTienNhan = tongTienNhan != null ? tongTienNhan.doubleValue() : 0.0;
    }

    public String getTenKhoaHoc() {
        return tenKhoaHoc;
    }

    public void setTenKhoaHoc(String tenKhoaHoc) {
        this.tenKhoaHoc = tenKhoaHoc;
    }

    public Long getSoLuotDangKy() {
        return soLuotDangKy;
    }

    public void setSoLuotDangKy(Long soLuotDangKy) {
        this.soLuotDangKy = soLuotDangKy;
    }

    public Double getTongTienGiaoDich() {
        return tongTienGiaoDich;
    }

    public void setTongTienGiaoDich(Double tongTienGiaoDich) {
        this.tongTienGiaoDich = tongTienGiaoDich;
    }

    public Double getTongTienNhan() {
        return tongTienNhan;
    }

    public void setTongTienNhan(Double tongTienNhan) {
        this.tongTienNhan = tongTienNhan;
    }
}