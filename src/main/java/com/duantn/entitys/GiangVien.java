package com.duantn.entitys;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

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
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "GiangVien")
public class GiangVien implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer giangvienId;

    @Column(name = "kyNang")
    private String kyNang;

    @Column(name = "kinhNghiem")
    private Integer kinhNghiem;

    @Column(name = "ngaythamgia", updatable = false)
    @CreationTimestamp
    private LocalDateTime ngayThamGia;

    @OneToOne
    @JoinColumn(name = "accountId", nullable = false)
    private Account accounts;

}
