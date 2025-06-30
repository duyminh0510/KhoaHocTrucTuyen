package com.duantn.entities;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "DanhGia")
public class DanhGia implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer danhgiaId;

    @Column(name = "NoiDung", nullable = false, columnDefinition = "NVARCHAR(MAX)")
    private String noiDung;

    @CreationTimestamp
    @Column(name = "ngayDanhGia", nullable = false, updatable = false)
    private LocalDateTime ngayDanhGia;

    @Column(name = "DiemDanhGia", nullable = false)
    private Integer diemDanhGia;

    @ManyToOne
    @JoinColumn(name = "taikhoanId", nullable = false)
    private TaiKhoan taikhoan;

    @ManyToOne
    @JoinColumn(name = "khoahocId", nullable = false)
    private KhoaHoc khoahoc;
}
