package com.duantn.entitys;

import java.io.Serializable;
import java.time.LocalDateTime;

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

    @Column(name = "NoiDung", nullable = false)
    private String noiDung;

    @Column(name = "NgayDanhGia", nullable = false)
    private LocalDateTime ngayDanhGia;

    @Column(name = "DiemDanhGia", nullable = false)
    private Integer diemDanhGia;

    @ManyToOne
    @JoinColumn(name = "accountId", nullable = false)
    private Account account;

    @ManyToOne
    @JoinColumn(name = "courseId", nullable = false)
    private Course courses;
}
