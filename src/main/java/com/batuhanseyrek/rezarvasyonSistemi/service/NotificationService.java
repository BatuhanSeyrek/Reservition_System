package com.batuhanseyrek.rezarvasyonSistemi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import com.batuhanseyrek.rezarvasyonSistemi.dto.response.ReservationResponse;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final SimpMessagingTemplate messagingTemplate;

    public void sendNewReservation(ReservationResponse dto, Long storeId) {
        messagingTemplate.convertAndSend("/topic/reservations/" + storeId, dto);
    }
}
