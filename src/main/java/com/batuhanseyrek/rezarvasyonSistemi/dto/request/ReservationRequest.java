package com.batuhanseyrek.rezarvasyonSistemi.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationRequest {


    private Long chairId;

    private LocalDate reservationDate;
    private LocalTime startTime;

    // Reference login için eklenen alanlar
    private String customerName;
    private String customerSurname;
    private String customerPhone;

    // Reference login ile geliyorsa userId boş olabilir
    private Long userId;
}
