package com.batuhanseyrek.rezarvasyonSistemi.service.admin.Impl;

import com.batuhanseyrek.rezarvasyonSistemi.dto.DtoConverter;
import com.batuhanseyrek.rezarvasyonSistemi.entity.adminEntity.Admin;
import com.batuhanseyrek.rezarvasyonSistemi.entity.adminEntity.DtoRegisterAdmin;
import com.batuhanseyrek.rezarvasyonSistemi.entity.adminEntity.Store;
import com.batuhanseyrek.rezarvasyonSistemi.repository.AdminRepository;
import com.batuhanseyrek.rezarvasyonSistemi.repository.StoreRepository;
import com.batuhanseyrek.rezarvasyonSistemi.service.admin.SupervisorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
public class SupervisorServiceImpl implements SupervisorService {
    @Autowired
    AdminRepository adminRepository;
    @Autowired
    StoreRepository storeRepository;
    @Override
    public List<DtoRegisterAdmin> adminList() {
        LocalDateTime now = LocalDateTime.now();

        List<Admin> admins = adminRepository.findByStatusAndEndTimeAfter(true, now);

        List<DtoRegisterAdmin> dtoList = new ArrayList<>();
        Store store;
        for (Admin admin : admins) {
            if (admin.isStatus()==true) {
                store= storeRepository.findByAdminId(admin.getId());
                DtoRegisterAdmin dto = new DtoRegisterAdmin();
                dto.setId(admin.getId());
                dto.setAdminName(admin.getAdminName());
                dto.setPhoneNumber(admin.getPhoneNumber());
                dto.setStatus(admin.isStatus());
                dto.setStoreName(store.getStoreName());
                dto.setStartTime(admin.getStartTime());
                dto.setEndTime(admin.getEndTime());
                dto.setPassword(admin.getPassword()); // şifreyi göndermek istemezsin

                dtoList.add(dto);
            }
        }

        return dtoList;
    }

    @Override
    public void statusChange(Long id) {
        Optional<Admin> adminOptional = adminRepository.findById(id);

        if (adminOptional.isEmpty()){
            throw new RuntimeException("Böyle bir kullanıcı yok");
        }

        Admin admin=adminOptional.get();
        admin.setStatus(!admin.isStatus());
        adminRepository.save(admin);

    }

    @Override
    public void increaseDate(Long id) {
        Optional<Admin> adminOptional = adminRepository.findById(id);

        if (adminOptional.isEmpty()){
            throw new RuntimeException("Böyle bir kullanıcı yok");
        }
        Admin admin=adminOptional.get();
        admin.setEndTime(admin.getEndTime().plusDays(30));

        adminRepository.save(admin);
    }

}
