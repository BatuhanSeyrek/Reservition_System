package com.batuhanseyrek.rezarvasyonSistemi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DtoStore {
    private Long id;
    private String storeName;
    private Integer chairCount;
    private Long adminId;
    private Long employeeId;
    private Long reservationsId;
}
