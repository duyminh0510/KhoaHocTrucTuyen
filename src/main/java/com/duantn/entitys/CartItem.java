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
@Table(name = "CartItem")
public class CartItem implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "DonGia", nullable = false)
    private Double donGia;

    @Column(name = "NgayThem", nullable = false)
    private LocalDateTime ngayThem;

    @ManyToOne
    @JoinColumn(name = "cartId", nullable = false)
    private Cart carts;

    @ManyToOne
    @JoinColumn(name = "courseId", nullable = false)
    private Course courses;
}
