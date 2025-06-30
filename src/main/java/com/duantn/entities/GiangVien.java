package com.duantn.entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Table(name = "GiangVien")
public class GiangVien implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer giangvienId;

    @Column(name = "kyNang", columnDefinition = "NVARCHAR(MAX)")
    private String kyNang;

    @Column(name = "kinhNghiem", columnDefinition = "NVARCHAR(MAX)")
    private String kinhNghiem;

    @Column(name = "CCCD", columnDefinition = "NVARCHAR(MAX)")
    private String CCCD;

    @Column(name = "congViec", columnDefinition = "NVARCHAR(255)")
    private String congViec;

    @Column(name = "ngaySinh")
    private LocalDate ngaySinh;

    @Column(name = "gioiTinh", columnDefinition = "NVARCHAR(50)")
    private String gioiTinh;

    @Column(name = "chuyenNganh", columnDefinition = "NVARCHAR(255)")
    private String chuyenNganh;

    @CreationTimestamp
    @Column(name = "ngaythamgia", updatable = false)
    private LocalDateTime ngayThamGia;

    @UpdateTimestamp
    @Column(name = "ngay_cap_nhat")
    private LocalDateTime ngayCapNhat;

    @OneToOne
    @JoinColumn(name = "taikhoanId", nullable = false, unique = true)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private TaiKhoan taikhoan;
}
