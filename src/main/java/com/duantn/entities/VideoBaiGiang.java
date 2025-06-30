package com.duantn.entities;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@EqualsAndHashCode(exclude = "baiGiang")
@Table(name = "VideoBaiGiang")
public class VideoBaiGiang implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;

    @Column(name = "url_video", columnDefinition = "NVARCHAR(MAX)")
    private String url_video;

    @Column(name = "mota", columnDefinition = "NVARCHAR(MAX)")
    private String mota;

    @CreationTimestamp
    @Column(name = "ngay_tao", updatable = false)
    private LocalDateTime ngayTao;

    @UpdateTimestamp
    @Column(name = "ngay_cap_nhat")
    private LocalDateTime ngayCapNhat;

    @Column(name = "trangThai")
    @Builder.Default
    private Boolean trangThai = true;

    @OneToOne
    @JoinColumn(name = "baiGiangId", unique = true)
    private BaiGiang baiGiang;
}
