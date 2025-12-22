package com.batuhanseyrek.rezarvasyonSistemi.dto.response;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class ReservationResponse {
    private Long id;
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalDate reservationDate;

    private String chairName;
    private Long chairId;

    private String userName;
    private String employeeName;

    private String storeName;
    private Long storeId;

    // 🔹 TEK TELEFON ALANI
    private String phoneNumber;
    private boolean guest;

}
