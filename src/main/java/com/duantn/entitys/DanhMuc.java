package com.duantn.entitys;

import java.util.Date;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "danhmuc")
public class DanhMuc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer danhmucId;

    @Column(nullable = false)
    private String tenDanhMuc;

    @Temporal(TemporalType.TIMESTAMP)
    private Date ngayTao = new Date();

    // Getter & Setter
    public Integer getDanhmucId() {
        return danhmucId;
    }

    public void setDanhmucId(Integer danhmucId) {
        this.danhmucId = danhmucId;
    }

    public String getTenDanhMuc() {
        return tenDanhMuc;
    }

    public void setTenDanhMuc(String tenDanhMuc) {
        this.tenDanhMuc = tenDanhMuc;
    }

    public Date getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(Date ngayTao) {
        this.ngayTao = ngayTao;
    }
}
