package com.duantn.entitys;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
@Table(name = "ThuNhapNenTang")
public class ThuNhapNenTang implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer thanhtoanId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "danghocId", nullable = false)
    private DangHoc dangHoc;

    @Column(name = "sotiennhan", precision = 12, scale = 2, nullable = false)
    private BigDecimal sotiennhan;

    @CreationTimestamp
    @Column(name = "ngaynhan", updatable = false)
    private LocalDateTime ngaynhan;

    @Column(name = "tenKhoaHoc")
    private String tenKhoaHoc;

    @Column(name = "thuocGiangVien")
    private String thuocGiangVien;
}
