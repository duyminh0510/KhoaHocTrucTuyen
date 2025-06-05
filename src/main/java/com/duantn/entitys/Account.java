package com.duantn.entitys;

import java.io.Serializable;
import java.time.LocalDateTime;

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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "Account")
public class Account implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "accountId")
    private Integer accountId;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @CreationTimestamp
    @Column(name = "ngay_tao", updatable = false)
    private LocalDateTime ngayTao;

    @UpdateTimestamp
    @Column(name = "ngay_cap_nhat")
    private LocalDateTime ngayCapNhat;

    @Column(name = "avatar")
    private String avatar;

    @Column(name = "password")
    private String password;

    // tài khoản mới thêm mặc định trạng thái là true
    @Column(name = "status", nullable = false, columnDefinition = "BIT DEFAULT 1")
    @Builder.Default
    private boolean status = true;

    // các mối quan hệ giữa các bảng
    @ManyToOne
    @JoinColumn(name = "roleId", nullable = false)
    private Role role;

    @OneToOne(mappedBy = "accounts", cascade = CascadeType.ALL)
    private GiangVien giangVien;
}