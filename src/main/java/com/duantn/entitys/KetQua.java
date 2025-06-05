package com.duantn.entitys;

import java.io.Serializable;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "KetQua")
public class KetQua implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ketquald;

    @Column(name = "ThoiGianLam", nullable = false)
    private Integer thoiGianLam;

    @Column(name = "TongDiem", nullable = false)
    private Double tongDiem;

    @Column(name = "Socaudung", nullable = false)
    private Integer soCauDung;
    @ManyToOne
    @JoinColumn(name = "accountId", nullable = false)
    private Account accounts;

    @ManyToOne
    @JoinColumn(name = "baiTapId", nullable = false)
    private BaiTap baiTaps;
}
