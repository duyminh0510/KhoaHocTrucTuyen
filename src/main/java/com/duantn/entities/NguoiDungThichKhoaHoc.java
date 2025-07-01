package com.duantn.entities;

<<<<<<< HEAD
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
=======
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
>>>>>>> 82b8d85276debf6d30035129ac4415f6a301d0a0

@Entity
@Table(name = "NguoiDungThichKhoaHoc")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NguoiDungThichKhoaHoc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "taikhoan_id", nullable = false)
    private TaiKhoan taiKhoan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "khoahoc_id", nullable = false)
    private KhoaHoc khoaHoc;
<<<<<<< HEAD
} 
=======
}
>>>>>>> 82b8d85276debf6d30035129ac4415f6a301d0a0
