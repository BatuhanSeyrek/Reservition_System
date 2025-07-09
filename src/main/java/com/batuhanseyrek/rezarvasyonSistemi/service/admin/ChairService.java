package com.batuhanseyrek.rezarvasyonSistemi.service.admin;

import com.batuhanseyrek.rezarvasyonSistemi.dto.response.DtoChair;
import com.batuhanseyrek.rezarvasyonSistemi.entity.adminEntity.Chair;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ChairService {
    DtoChair chairAdd(DtoChair request, HttpServletRequest httpRequest);
    List<DtoChair> chairList();
    void chairDelete(Long id);
    ResponseEntity<?> chairUpdate(Long id, Chair chair);
}
