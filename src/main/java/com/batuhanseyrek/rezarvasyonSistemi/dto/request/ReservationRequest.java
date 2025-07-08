package com.batuhanseyrek.rezarvasyonSistemi.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationRequest {

    private Long chairId;
    private LocalDate reservationDate;
    private LocalTime startTime;
}
