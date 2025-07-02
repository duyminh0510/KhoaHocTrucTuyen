package com.duantn.entities;

import java.io.Serializable;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "Role")
public class Role implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "roleId")
    private Integer roleId;

    @Column(name = "name", columnDefinition = "NVARCHAR(MAX)")
    private String name;

    // các mối quan hệ giữa các bảng

    @OneToMany(mappedBy = "role")
    private List<TaiKhoan> taikhoans;

    @Override
    public String toString() {
        return "Role{" +
                "roleId=" + roleId +
                ", name='" + name + '\'' +
                '}';
    }

}