package com.duantn.entitys;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "LuaChon")
public class LuaChon implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer luaChonId;

    @Column(name = "NoiDung_DapAn", nullable = false)
    private String noiDungDapAn;

    @Column(name = "ThuTu_DapAn")
    private Integer thuTuDapAn;

    @Column(name = "DapAnDung")
    private Boolean dapAnDung;

    @Column(name = "GiaThich_Dapan")
    private String giaThichDapan;

    @CreationTimestamp
    @Column(name = "ngay_tao", updatable = false)
    private LocalDateTime ngayTao;

    @UpdateTimestamp
    @Column(name = "ngay_cap_nhat")
    private LocalDateTime ngayCapNhat;

    @ManyToOne
    @JoinColumn(name = "CauHoild", nullable = false)
    private CauHoi cauHoi;

}
