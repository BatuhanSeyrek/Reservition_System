package com.batuhanseyrek.rezarvasyonSistemi.service.user;

import com.batuhanseyrek.rezarvasyonSistemi.dto.request.ReservationRequest;
import com.batuhanseyrek.rezarvasyonSistemi.dto.response.DtoAdminFull;
import com.batuhanseyrek.rezarvasyonSistemi.dto.response.ReservationResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

public interface StoreService {
    List<DtoAdminFull> storeAll();
    ReservationResponse rezrvationAdd(ReservationRequest request, HttpServletRequest httpRequest);
    List<ReservationResponse> reservationList();
    void reservationDelete(Long id);
    ReservationResponse reservationUpdate(ReservationResponse request, Long id, HttpServletRequest httpServletRequest);
    List<ReservationResponse> userReservationGet(HttpServletRequest httpServletRequest);
    void userReservationDelete(HttpServletRequest httpServletRequest,Long id);
    Map<LocalDate, Map<LocalTime, Boolean>> getAvailableSlots(HttpServletRequest httpServletRequest, Long id);
}
