package com.duantn.dtos;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DapAnDto {
    private String noiDungDapAn;
    private boolean dapAnDung;
    private String giaThichDapan;
    private Integer cauHoiId;
    private Integer thuTuDapAn;
}
