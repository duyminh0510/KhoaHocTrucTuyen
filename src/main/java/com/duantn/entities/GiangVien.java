package com.duantn.entities;

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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
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

    @CreationTimestamp
    @Column(name = "ngaythamgia", updatable = false)
    private LocalDateTime ngayThamGia;

    @UpdateTimestamp
    @Column(name = "ngay_cap_nhat")
    private LocalDateTime ngayCapNhat;

    @OneToOne
    @JoinColumn(name = "taikhoanId", nullable = false)
    private TaiKhoan taikhoan;

    @Override
    public String toString() {
        return "GiangVien{" +
                "giangvienId=" + giangvienId +
                ", kyNang='" + kyNang + '\'' +
                ", kinhNghiem='" + kinhNghiem + '\'' +
                ", CCCD='" + CCCD + '\'' +
                ", ngayThamGia=" + ngayThamGia +
                ", ngayCapNhat=" + ngayCapNhat +
                ", taikhoanId=" + (taikhoan != null ? taikhoan.getTaikhoanId() : "null") +
                '}';
    }
}
