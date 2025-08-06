package com.batuhanseyrek.rezarvasyonSistemi.dto.response;

import lombok.Data;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;
@Data
public class ReservationResponse {
    private Long id;
    private LocalTime startTime;
    private LocalTime endTime;
    private String chairName;
    private String userName;
    private String employeeName;
    private LocalDate reservationDate;
}
