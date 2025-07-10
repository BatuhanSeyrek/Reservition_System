package com.batuhanseyrek.rezarvasyonSistemi.entity.userEntity;

import com.batuhanseyrek.rezarvasyonSistemi.entity.NotificationType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;

@Entity
@Table(name = "user")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String email;
    private String phoneNumber;
    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;
    @Column(name="userName",nullable = false)
    private String userName;
    @Column(name="password",nullable = false)
    private String password;
}
