package com.batuhanseyrek.rezarvasyonSistemi.service.admin;

import com.batuhanseyrek.rezarvasyonSistemi.dto.request.AuthRequest;
import com.batuhanseyrek.rezarvasyonSistemi.dto.response.DtoAdmin;
import com.batuhanseyrek.rezarvasyonSistemi.entity.adminEntity.Admin;
import com.batuhanseyrek.rezarvasyonSistemi.entity.adminEntity.DtoRegisterAdmin;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface AdminService {
    DtoRegisterAdmin myApp(HttpServletRequest request);
    Map<String,Object> mapping(AuthRequest request);
    ResponseEntity<String> register(DtoRegisterAdmin request);
    List<DtoAdmin> adminList();
    void adminDelete(Long id);
    ResponseEntity<?> adminUpdate(DtoRegisterAdmin request, HttpServletRequest httpRequest);
}
