package com.batuhanseyrek.rezarvasyonSistemi.entity.adminEntity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DtoRegisterAdmin {
    private Long id;
    private String adminName;
    private String password;
    private String phoneNumber;
    private boolean status;
    private String referenceId;
    private boolean referenceStatus;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime startTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime endTime;
    private String storeName;
    private int chairCount;
    private String city;
    private String district;
}
