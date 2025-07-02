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
@Table(name = "BaiViet")
public class BaiViet implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer baivietId;

    @Column(length = 10000, columnDefinition = "NVARCHAR(MAX)")
    private String noidung;

    @CreationTimestamp
    @Column(name = "ngay_tao", updatable = false)
    private LocalDateTime ngayTao;

    @UpdateTimestamp
    @Column(name = "ngay_cap_nhat")
    private LocalDateTime ngayCapNhat;

    @Column(name = "trangthai")
    private Boolean trangthai = true;

    @OneToOne
    @JoinColumn(name = "baiGiangId", unique = true)
    private BaiGiang baiGiang;

}
