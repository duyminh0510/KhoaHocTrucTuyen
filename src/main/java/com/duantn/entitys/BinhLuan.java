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
@Table(name = "BinhLuan")
public class BinhLuan implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer binhluanId;

    @Column(name = "NoiDung", nullable = false)
    private String noiDung;

    @Column(name = "NgayBinhLuan", nullable = false)
    private LocalDateTime ngayBinhLuan;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private BinhLuan parent;

    @ManyToOne
    @JoinColumn(name = "accountId", nullable = false)
    private Account account;

    @ManyToOne
    @JoinColumn(name = "baiGiangId", nullable = false)
    private BaiGiang baiGiang;
}
