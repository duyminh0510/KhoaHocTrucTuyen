package com.duantn.entitys;

import java.io.Serializable;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "GiaoDichKhoaHoc_Detail")
public class GiaoDichKhoaHoc_Detail implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "DonGia", nullable = false)
    private Double donGia;

    @ManyToOne
    @JoinColumn(name = "giaodichId", nullable = false)
    private GiaoDichKhoaHoc giaoDichKhoaHoc;

    @ManyToOne
    @JoinColumn(name = "courseId", nullable = false)
    private Course courses;
}
