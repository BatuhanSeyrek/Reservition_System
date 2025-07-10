package com.batuhanseyrek.rezarvasyonSistemi.service.mail;

import com.batuhanseyrek.rezarvasyonSistemi.entity.NotificationType;
import com.batuhanseyrek.rezarvasyonSistemi.entity.userEntity.Reservation;
import com.batuhanseyrek.rezarvasyonSistemi.repository.ReservationRepository;
import com.batuhanseyrek.rezarvasyonSistemi.service.mail.MailService;
import com.batuhanseyrek.rezarvasyonSistemi.service.sms.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class ReminderScheduler {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private MailService mailService;

    @Autowired
    private SmsService smsService;

    @Scheduled(cron = "0 * * * * *") // Her dakika başında çalışır
    public void sendReminder() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime target = now.plusMinutes(30);

        List<Reservation> reservations = reservationRepository.findAll();

        for (Reservation r : reservations) {
            LocalDateTime resTime = LocalDateTime.of(r.getReservationDate(), r.getStartTime());

            long minutesDiff = ChronoUnit.MINUTES.between(now, resTime);
            if (!r.isReminderSent() && minutesDiff == 30) {

                String name = r.getUser().getUserName();
                String chair = r.getChair().getChairName();
                String message = "Merhaba " + name + ",\n\n" +
                        chair + " için randevunuz " + r.getStartTime() + "’da başlayacak. Lütfen zamanında hazır olun.";

                NotificationType type = r.getUser().getNotificationType();

                if (type == NotificationType.SMS || type == NotificationType.BOTH) {
                    smsService.sendSms(r.getUser().getPhoneNumber(), message);
                }

                if (type == NotificationType.MAIL || type == NotificationType.BOTH) {
                    String html = "<h3>Randevu Hatırlatma</h3><p>" + message.replace("\n", "<br/>") + "</p>";
                    mailService.sendHtmlMail(r.getUser().getEmail(), "Randevu Hatırlatma", html);
                }

                r.setReminderSent(true);
                reservationRepository.save(r);
            }
        }
    }
}
