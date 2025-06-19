package com.duantn.entitys;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.duantn.enums.LoaiBaiGiang;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "BaiGiang")
public class BaiGiang implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer baiGiangId;

    @Column(name = "tenBaiGiang")
    private String tenBaiGiang;

    @Column(length = 10000)
    private String mota;

    @CreationTimestamp
    @Column(name = "ngay_tao", updatable = false)
    private LocalDateTime ngayTao;

    @UpdateTimestamp
    @Column(name = "ngay_cap_nhat")
    private LocalDateTime ngayCapNhat;

    @Column(name = "trangthai")
    private Boolean trangthai;

    @Enumerated(EnumType.STRING)
    @Column(name = "loai_bai_giang", nullable = false, length = 30)
    private LoaiBaiGiang loaiBaiGiang;

    @ManyToOne
    @JoinColumn(name = "chuongId")
    private Chuong chuongs;

    @OneToOne(mappedBy = "baiGiang")
    private BaiViet baiViet;
}
