package com.batuhanseyrek.rezarvasyonSistemi.service.user;

import com.batuhanseyrek.rezarvasyonSistemi.dto.request.ReservationRequest;
import com.batuhanseyrek.rezarvasyonSistemi.dto.response.DtoAdminFull;
import com.batuhanseyrek.rezarvasyonSistemi.dto.response.DtoChair;
import com.batuhanseyrek.rezarvasyonSistemi.dto.response.ReservationResponse;
import com.batuhanseyrek.rezarvasyonSistemi.entity.userEntity.ReferenceLoginRequest;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface ReservationService {
    List<DtoAdminFull> storeAll();
    ReservationResponse rezrvationAdd(ReservationRequest request, HttpServletRequest httpRequest);
    List<ReservationResponse> reservationList();
    void reservationDelete(Long id);
    ReservationResponse reservationUpdate(ReservationResponse request, Long id, HttpServletRequest httpServletRequest);
    List<ReservationResponse> userReservationGet(HttpServletRequest httpServletRequest);
    void userReservationDelete(HttpServletRequest httpServletRequest,Long id);
    List<Map<String, Object>> getAvailableSlots(Long id);
    List<Map<String, Object>> getAvailableSlotsReference(ReferenceLoginRequest referenceId);
    List<DtoChair> getChairsByStore(Long storeId);
    List<ReservationResponse> getReservationsForAdmin(HttpServletRequest request);
    byte[] exportReservationsToExcel(HttpServletRequest request) throws IOException;

}
