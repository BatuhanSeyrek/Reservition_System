package com.batuhanseyrek.rezarvasyonSistemi.dto.response;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DtoAddress {
    private Long id;
    private Long admin_id;
    private String city;
    private String district;
}
