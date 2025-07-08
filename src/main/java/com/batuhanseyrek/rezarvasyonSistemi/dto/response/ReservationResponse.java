package com.batuhanseyrek.rezarvasyonSistemi.dto.response;

import lombok.Data;

import java.time.LocalTime;
@Data
public class ReservationResponse {

    private LocalTime startTime;
    private LocalTime endTime;
    private String chairName;
    private String userName;
    private String employeeName;
}
