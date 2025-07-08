package com.batuhanseyrek.rezarvasyonSistemi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DtoAdmin {
    private Long id;
    private String adminName;
    private Integer chairCount;
    private String storeName;
}
