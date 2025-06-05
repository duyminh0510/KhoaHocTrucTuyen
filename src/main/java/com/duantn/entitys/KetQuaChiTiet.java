package com.duantn.entitys;

import java.io.Serializable;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "KetQuaChiTiet")
public class KetQuaChiTiet implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "DapAnDaChon", nullable = false)
    private String dapAnDaChon;

    @Column(name = "diem", nullable = false)
    private Double diem;

    @Column(name = "DungSai")
    private Boolean dungSai;

    @ManyToOne
    @JoinColumn(name = "ketquald", nullable = false)
    private KetQua ketQua;

    @ManyToOne
    @JoinColumn(name = "cauHoiId", nullable = false)
    private CauHoi cauHoi;

    @ManyToOne
    @JoinColumn(name = "luaChonId", nullable = false)
    private LuaChon luaChon;

}
