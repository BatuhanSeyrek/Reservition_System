package com.batuhanseyrek.rezarvasyonSistemi.entity.userEntity;

import com.batuhanseyrek.rezarvasyonSistemi.entity.adminEntity.Chair;
import com.batuhanseyrek.rezarvasyonSistemi.entity.adminEntity.Store;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "reservation")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean reminderSent = false;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "chair_id", nullable = false)
    private Chair chair;

    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Column(name = "reservation_date", nullable = false)
    private LocalDate reservationDate;
}
