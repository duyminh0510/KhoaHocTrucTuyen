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
@Table(name = "DangKyKhoaHoc")
public class DangKyKhoaHoc implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "NgayDangKy", nullable = false)
    private LocalDateTime ngayDangKy;

    @Column(name = "TienDo", nullable = false)
    private Integer tienDo;

    @Column(name = "status", nullable = false)
    private Boolean status;

    @ManyToOne
    @JoinColumn(name = "accountId", nullable = false)
    private Account accounts;

    @ManyToOne
    @JoinColumn(name = "courseId", nullable = false)
    private Course courses;
}
