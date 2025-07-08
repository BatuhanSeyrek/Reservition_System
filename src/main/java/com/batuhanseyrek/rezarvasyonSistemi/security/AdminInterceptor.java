package com.batuhanseyrek.rezarvasyonSistemi.security;

import com.batuhanseyrek.rezarvasyonSistemi.repository.AdminRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AdminInterceptor implements HandlerInterceptor {

    // Buraya kendi JWT util veya servisini ekleyebilirsin
    private final JwtUtil jwtUtil;
    private final AdminRepository adminRepository;


    public AdminInterceptor(JwtUtil jwtUtil, AdminRepository adminRepository) {
        this.jwtUtil = jwtUtil;
        this.adminRepository = adminRepository;
    }
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("Authorization header yok veya hatalı");
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Yetkisiz erişim - Authorization header eksik veya yanlış");
            return false; // 403 gönder ve işlemi kes
        }

        String token = authHeader.substring(7);
        String adminName = jwtUtil.extraUserName(token);
        System.out.println("Token'dan adminName: " + adminName);

        if (adminName == null) {
            System.out.println("Token geçersiz veya adminName alınamadı");
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Yetkisiz erişim - Token geçersiz");
            return false;
        }

        // AdminInterceptor.java içinde
        Long adminId = adminRepository.findByAdminName(adminName)
                .map(admin -> admin.getId()) // Eğer Admin bulunur ve getId() metodu Long döndürürse
                .orElse(null); // Eğer Admin bulunamazsa adminId null olur

// Şimdi adminId'nin null olup olmadığını kontrol edebilirsiniz
        if (adminId == null) {
            System.err.println("AdminInterceptor: Token'dan gelen adminName '" + adminName + "' ile Admin bulunamadı.");
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Yetkisiz erişim - Admin bulunamadı");
            return false;
        }

        request.setAttribute("adminId", adminId);

        return true;
    }

}
