package com.duantn.entitys;

import java.time.LocalDateTime;

import com.duantn.enums.LoaiThanhToan;
import com.duantn.enums.TrangThaiThanhToan;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "ThanhToan")
public class ThanhToan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer thanhtoanId;

    @Column(name = "PhuongThuc", nullable = false)
    private String phuongThuc;

    @Column(name = "DonGia", nullable = false)
    private Double donGia;

    @Column(name = "NgayThanhToan", nullable = false)
    private LocalDateTime ngayThanhToan;

    @Enumerated(EnumType.STRING)
    @Column(name = "loaiThanhToan", nullable = false)
    private LoaiThanhToan loaiThanhToan;

    @Enumerated(EnumType.STRING)
    @Column(name = "trangThaiThanhToan", nullable = false)
    private TrangThaiThanhToan trangThaiThanhToan;

    @ManyToOne
    @JoinColumn(name = "giaodichId", nullable = false)
    private GiaoDichKhoaHoc giaoDichKhoaHoc;
}
