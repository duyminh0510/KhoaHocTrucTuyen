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
@Table(name = "ChungChi")
public class ChungChi implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer chungchiId;

    @Column(name = "MaChungChi", nullable = false, unique = true)
    private String maChungChi;

    @Column(name = "NgayCap", nullable = false)
    private LocalDateTime ngayCap;

    @Column(name = "DuongDanFile", nullable = false)
    private String duongDanFile;

    @ManyToOne
    @JoinColumn(name = "accountId", nullable = false)
    private Account accounts;

    @ManyToOne
    @JoinColumn(name = "courseId", nullable = false)
    private Course courses;
}
