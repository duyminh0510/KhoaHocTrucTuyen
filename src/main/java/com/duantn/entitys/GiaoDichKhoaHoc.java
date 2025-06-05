package com.duantn.entitys;

import java.time.LocalDateTime;

import com.duantn.enums.HinhThucThanhToan;
import com.duantn.enums.TrangThaiGiaoDich;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "GiaoDichKhoaHoc")
public class GiaoDichKhoaHoc {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer giaodichId;

    @Column(name = "TongTien", nullable = false)
    private Double tongTien;

    @Column(name = "NgayGiaoDich", nullable = false)
    private LocalDateTime ngayGiaoDich;

    @Enumerated(EnumType.STRING)
    @Column(name = "hinhThucThanhToan", nullable = false)
    private HinhThucThanhToan hinhThucThanhToan;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TrangThaiGiaoDich status;

    @ManyToOne
    @JoinColumn(name = "accountId", nullable = false)
    private Account accounts;
}
