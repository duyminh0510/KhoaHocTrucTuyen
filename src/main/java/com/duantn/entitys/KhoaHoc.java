package com.duantn.entitys;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Course")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KhoaHoc implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id")
    private Integer courseId;

    @Column(name = "url_gioi_thieu", length = 500)
    private String urlGioiThieu;

    @Column(name = "giagoc", precision = 12, scale = 2, nullable = false)
    private BigDecimal giagoc;

    @Column(name = "gia_khuyen_mai", precision = 12, scale = 2)
    private BigDecimal giaKhuyenMai;

    @Column(name = "phan_tram_giam")
    private Integer phanTramGiam;

    @Column(name = "ngaybatdau")
    private LocalDateTime ngaybatdau;

    @Column(name = "ngayketthuc")
    private LocalDateTime ngayketthuc;

    @Column(length = 10000)
    private String moTa;

    @Column(name = "luot_thich")
    private Integer luotThich;

    @Column(name = "ten_khoa_hoc", nullable = false)
    private String tenKhoaHoc;

    @Column(name = "anh_bia", length = 500)
    private String anhBia;

    @Column(name = "trang_thai", nullable = false)
    private Boolean trangThai;

    @CreationTimestamp
    @Column(name = "ngay_tao", updatable = false)
    private LocalDateTime ngayTao;

    @Column(length = 255)
    private String share;

    // ===== Quan hệ =====

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "giangvien_id", nullable = false)
    private GiangVien giangVien;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "danhmuc_id", nullable = false)
    private DanhMuc danhMuc;

    @OneToMany(mappedBy = "courses", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Chuong> chuongs;
}
