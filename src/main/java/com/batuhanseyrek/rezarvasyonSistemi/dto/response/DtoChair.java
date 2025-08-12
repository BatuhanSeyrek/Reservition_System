package com.batuhanseyrek.rezarvasyonSistemi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DtoChair {
    private Long id;
    private LocalTime openingTime;
    private LocalTime closingTime;
    private LocalTime islemSuresi;
    private String chairName;
    private Long adminId;
    private Long storeId;
}
