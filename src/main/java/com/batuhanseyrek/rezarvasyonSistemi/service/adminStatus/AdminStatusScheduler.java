package com.batuhanseyrek.rezarvasyonSistemi.service.adminStatus;

import com.batuhanseyrek.rezarvasyonSistemi.entity.adminEntity.Admin;
import com.batuhanseyrek.rezarvasyonSistemi.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class AdminStatusScheduler {

    @Autowired
    private AdminRepository adminRepository;

    @Scheduled(cron = "0 0 0 * * *") // Her gün gece yarısı çalışır
    public void deactivateAdminsWithEndTimeToday() {
        LocalDate today = LocalDate.now();
        List<Admin> admins = adminRepository.findAll();

        for (Admin admin : admins) {
            if (admin.getEndTime() != null && admin.getEndTime().toLocalDate().isEqual(today)) {
                admin.setStatus(false); // status 0 yerine boolean false
                adminRepository.save(admin);
            }
        }
    }
}
