package com.batuhanseyrek.rezarvasyonSistemi.dto;

import com.batuhanseyrek.rezarvasyonSistemi.entity.NotificationType;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DtoUser {
    private Integer id;
    private String email;
    private String phoneNumber;
    private NotificationType notificationType;
    private String userName;
    private String password;
}
