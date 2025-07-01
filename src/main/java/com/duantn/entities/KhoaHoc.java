package com.duantn.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import com.duantn.enums.TrangThaiKhoaHoc;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "KhoaHoc")
public class KhoaHoc implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "khoahocId")
    private Integer khoahocId;

    @Column(name = "url_gioi_thieu", length = 500, columnDefinition = "NVARCHAR(MAX)")
    private String urlGioiThieu;

    @Column(name = "giagoc", precision = 12, scale = 2, nullable = true)
    private BigDecimal giagoc;

    @Column(name = "gia_khuyen_mai", precision = 12, scale = 2)
    private BigDecimal giaKhuyenMai;

    @Column(name = "phan_tram_giam")
    private Integer phanTramGiam;

    @Column(name = "ngaybatdau")
    private LocalDateTime ngaybatdau;

    @Column(name = "ngayketthuc")
    private LocalDateTime ngayketthuc;

    @Column(length = 10000, columnDefinition = "NVARCHAR(MAX)")
    private String moTa;

    @Column(name = "luot_thich")
    private Integer luotThich;

    @Column(name = "ten_khoa_hoc", columnDefinition = "NVARCHAR(MAX)")
    private String tenKhoaHoc;

    @Column(name = "anh_bia", length = 500, columnDefinition = "NVARCHAR(MAX)")
    private String anhBia;

    @Column(name = "anh_bia_public_id", length = 500, columnDefinition = "NVARCHAR(MAX)")
    private String anhBiaPublicId;

    @Enumerated(EnumType.STRING)
    @Column(name = "trang_thai")
    private TrangThaiKhoaHoc trangThai;

    @CreationTimestamp
    @Column(name = "ngay_tao", updatable = false)
    private LocalDateTime ngayTao;

    @Column(length = 255, columnDefinition = "NVARCHAR(MAX)")
    private String share;

    // ===== Quan hệ =====

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "giangvien_id", nullable = false)
    private GiangVien giangVien;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "danhmuc_id", nullable = false)
    private DanhMuc danhMuc;

    @OneToMany(mappedBy = "khoahoc", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Chuong> chuongs;

    @OneToMany(mappedBy = "khoahoc", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<GiaoDichKhoaHocChiTiet> giaoDichChiTiet;

    @Override
    public String toString() {
        return "KhoaHoc{" +
                "khoahocId=" + khoahocId +
                ", tenKhoaHoc='" + tenKhoaHoc + '\'' +
                ", giagoc=" + giagoc +
                ", giaKhuyenMai=" + giaKhuyenMai +
                ", phanTramGiam=" + phanTramGiam +
                ", ngaybatdau=" + ngaybatdau +
                ", ngayketthuc=" + ngayketthuc +
                ", trangThai=" + trangThai +
                ", giangVienId=" + (giangVien != null ? giangVien.getGiangvienId() : "null") +
                ", danhMucId=" + (danhMuc != null ? danhMuc.getDanhmucId() : "null") +
                '}';
    }
}
