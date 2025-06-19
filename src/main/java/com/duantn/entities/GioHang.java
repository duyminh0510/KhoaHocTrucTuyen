package com.duantn.entities;

import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "Cart")
public class GioHang implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer cartId;

    @Column(name = "NgayTao", nullable = false)
    private LocalDateTime ngayTao;

    @ManyToOne
    @JoinColumn(name = "accountId", nullable = false)
    private TaiKhoan account;
}
