package com.duantn.entitys;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "BaiGiang")
public class BaiGiang implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer baiGiangId;

    @Column(name = "trangThai")
    private Boolean trangThai;

    @Column(name = "tenBaiGiang")
    private String tenBaiGiang;

    @Column(length = 2000)
    private String noiDung;

    @Column(name = "urlVideo")
    private String urlVideo;

    @Column(name = "taiLieu")
    private String taiLieu;

    @CreationTimestamp
    @Column(name = "ngay_tao", updatable = false)
    private LocalDateTime ngayTao;

    @UpdateTimestamp
    @Column(name = "ngay_cap_nhat")
    private LocalDateTime ngayCapNhat;

    @ManyToOne
    @JoinColumn(name = "courseId")
    private Course courses;

    @OneToMany(mappedBy = "baiTapId", cascade = CascadeType.ALL)
    private List<BaiTap> baiTaps;

}
