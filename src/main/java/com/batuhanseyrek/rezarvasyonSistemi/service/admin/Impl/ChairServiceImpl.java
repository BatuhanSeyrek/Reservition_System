package com.batuhanseyrek.rezarvasyonSistemi.service.admin.Impl;

import com.batuhanseyrek.rezarvasyonSistemi.dto.DtoConverter;
import com.batuhanseyrek.rezarvasyonSistemi.dto.response.DtoChair;
import com.batuhanseyrek.rezarvasyonSistemi.entity.adminEntity.Admin;
import com.batuhanseyrek.rezarvasyonSistemi.entity.adminEntity.Chair;
import com.batuhanseyrek.rezarvasyonSistemi.repository.AdminRepository;
import com.batuhanseyrek.rezarvasyonSistemi.repository.ChairRepository;
import com.batuhanseyrek.rezarvasyonSistemi.repository.EmployeeRepository;
import com.batuhanseyrek.rezarvasyonSistemi.service.admin.ChairService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ChairServiceImpl implements ChairService {
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private ChairRepository chairRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private DtoConverter dtoConverter;
    @Override
    public DtoChair chairAdd(DtoChair request, HttpServletRequest httpRequest) {
        Long adminId = null;
        Object attr = httpRequest.getAttribute("adminId"); // <-- Interceptor'dan gelen değer
        Admin admin = adminRepository.findById((Long) attr)
                .orElseThrow(() -> new RuntimeException("Admin bulunamadı"));

        Chair chair = new Chair();
        chair.setChairName(request.getChairName());
        chair.setOpeningTime(request.getOpeningTime());
        chair.setClosingTime(request.getClosingTime());
        chair.setIslemSuresi(request.getIslemSuresi());
        chair.setAdmin(admin);

        Chair savedChair = chairRepository.save(chair);
        List<Chair> chairs = chairRepository.findAll();
        int deger=0;
        for (Chair chairs1: chairs){
            deger++;
        }
        admin.setChairCount(deger);
        adminRepository.save(admin);
        return DtoConverter.toDto(savedChair);

    }
    @Override
    public List<DtoChair> chairList() {
        List<Chair> chair =  chairRepository.findAll();
        return chair.stream().map(DtoConverter::toDto).collect(Collectors.toList());
    }
    @Override
    public void chairDelete(Long id) {
        chairRepository.deleteById(id);
    }
    @Override
    public ResponseEntity<?> chairUpdate(Long id, Chair chair) {
        Optional<Chair> existingChairOptional = chairRepository.findById(id);
        if (existingChairOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("Böyle bir sandalye yok");
        }

        Chair newChair = existingChairOptional.get();

        if (chair.getChairName() != null) {
            newChair.setChairName(chair.getChairName());
        }
        if (chair.getOpeningTime() != null) {
            newChair.setOpeningTime(chair.getOpeningTime());
        }
        if (chair.getClosingTime() != null) {
            newChair.setClosingTime(chair.getClosingTime());
        }
        if (chair.getEmployee() != null) {
            newChair.setEmployee(chair.getEmployee());
        }
        if (chair.getIslemSuresi() != null) {
            newChair.setIslemSuresi(chair.getIslemSuresi());
        }

        chairRepository.save(newChair);
        return ResponseEntity.ok(newChair);
    }

}
