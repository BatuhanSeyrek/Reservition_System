package com.batuhanseyrek.rezarvasyonSistemi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DtoChair {
    private Long id;
    private Time openingTime;
    private Time closingTime;
    private Time islemSuresi;
    private String chairName;
    private Long adminId;

}
