package com.batuhanseyrek.rezarvasyonSistemi.entity.adminEntity;

import lombok.*;

import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DtoRegisterAdmin {
    private Long id;
    private String adminName;
    private String password;
    private String phoneNumber;
    private LocalTime startTime;
    private LocalTime endTime;
    private String storeName;
    private int chairCount;
}
