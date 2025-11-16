package com.batuhanseyrek.rezarvasyonSistemi.repository;

import com.batuhanseyrek.rezarvasyonSistemi.entity.adminEntity.Chair;
import com.batuhanseyrek.rezarvasyonSistemi.entity.userEntity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByChair(Chair chair);

    @Query("SELECT r FROM Reservation r WHERE r.chair.id = :chairId AND r.reservationDate = :reservationDate AND r.startTime < :endTime AND r.endTime > :startTime")
    List<Reservation> findConflictingReservationsByDate(
            @Param("chairId") Long chairId,
            @Param("reservationDate") LocalDate reservationDate,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime);

    List<Reservation> findByChairAndReservationDate(Chair chair, LocalDate currentDate);

    @Query("SELECT r FROM Reservation r WHERE r.startTime BETWEEN :start AND :end AND r.reminderSent = false")
    List<Reservation> findReservationsBetween(LocalDateTime start, LocalDateTime end);

    // storeId'ye göre getir
    List<Reservation> findByStoreId(Long storeId);
}
