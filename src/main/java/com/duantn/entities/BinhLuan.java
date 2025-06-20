package com.duantn.entities;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "BinhLuan")
public class BinhLuan implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer binhluanId;

    @Column(name = "NoiDung", nullable = false)
    private String noiDung;

    @CreationTimestamp
    @Column(name = "NgayBinhLuan", nullable = false, updatable = false)
    private LocalDateTime ngayBinhLuan;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private BinhLuan parent;

    @ManyToOne
    @JoinColumn(name = "accountId", nullable = false)
    private TaiKhoan account;

    @ManyToOne
    @JoinColumn(name = "baiGiangId", nullable = false)
    private BaiGiang baiGiang;
}
