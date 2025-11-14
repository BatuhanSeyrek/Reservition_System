package com.batuhanseyrek.rezarvasyonSistemi.service.admin;

import com.batuhanseyrek.rezarvasyonSistemi.entity.adminEntity.DtoRegisterAdmin;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface SupervisorService {
    List<DtoRegisterAdmin> adminList();
    void statusChange(Long id);
    void increaseDate(Long id);
}
