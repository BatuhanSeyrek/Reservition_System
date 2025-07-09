package com.batuhanseyrek.rezarvasyonSistemi.service.mail;

import com.batuhanseyrek.rezarvasyonSistemi.entity.userEntity.Reservation;
import com.batuhanseyrek.rezarvasyonSistemi.entity.userEntity.User;
import com.batuhanseyrek.rezarvasyonSistemi.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class ReminderScheduler {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private MailService mailService;

    @Scheduled(cron = "0 * * * * *") // Her dakika başında çalışır
    public void sendReminderEmails() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime targetTime = now.plusMinutes(30);

        LocalDateTime windowStart = targetTime.minusSeconds(30);
        LocalDateTime windowEnd = targetTime.plusSeconds(30);

        List<Reservation> reservations = reservationRepository.findAll(); // Filtreyi manuel yapacağız

        for (Reservation reservation : reservations) {

            // Tarih + saat birleştirme
            LocalDateTime reservationDateTime = LocalDateTime.of(reservation.getReservationDate(), reservation.getStartTime());

            if (!reservation.isReminderSent()
                    && reservationDateTime.isAfter(windowStart)
                    && reservationDateTime.isBefore(windowEnd)) {

                String email = reservation.getUser().getEmail();
                String name = reservation.getUser().getUserName();
                String chair = reservation.getChair().getChairName();

                String subject = "Randevu Hatırlatma";
                String text = "Merhaba " + name + ",\n\n" +
                        "Randevunuz " + chair + " için saat " + reservation.getStartTime() + "’da başlayacak.\n" +
                        "Lütfen zamanında hazır olun.";

                try {
                    mailService.sendMail(email, subject, text);
                    reservation.setReminderSent(true);
                    reservationRepository.save(reservation);
                } catch (Exception e) {
                    e.printStackTrace(); // logla
                }
            }
        }
    }

}
