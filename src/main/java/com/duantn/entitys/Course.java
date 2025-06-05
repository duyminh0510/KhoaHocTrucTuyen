package com.duantn.entitys;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "Course")
public class Course implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer courseId;

    private String url_GioiThieu;

    private Double donGia;

    @Column(length = 1000)
    private String moTa;

    private Integer luotThich;

    private String tenKhoaHoc;

    private String anhBia;

    private Boolean trangThai;

    @CreationTimestamp
    @Column(name = "ngay_tao", updatable = false)
    private LocalDateTime ngayTao;

    @UpdateTimestamp
    @Column(name = "ngay_cap_nhat")
    private LocalDateTime ngayCapNhat;

    private String share;

    // các mối quan hệ
    @ManyToOne
    @JoinColumn(name = "giangvienId", nullable = false)
    private GiangVien giangVien;

    @ManyToOne
    @JoinColumn(name = "danhmucId", nullable = false)
    private DanhMuc danhMuc;

}
