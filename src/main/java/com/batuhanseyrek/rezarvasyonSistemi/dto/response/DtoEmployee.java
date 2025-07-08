package com.batuhanseyrek.rezarvasyonSistemi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DtoEmployee {
    private Long id;
    private String employeeName;
    private Long adminId;
    private Long chairId;
}
