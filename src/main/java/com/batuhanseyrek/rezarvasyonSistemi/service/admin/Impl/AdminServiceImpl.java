package com.batuhanseyrek.rezarvasyonSistemi.service.admin.Impl;

import com.batuhanseyrek.rezarvasyonSistemi.dto.DtoConverter;
import com.batuhanseyrek.rezarvasyonSistemi.dto.request.AuthRequest;
import com.batuhanseyrek.rezarvasyonSistemi.dto.response.DtoAdmin;
import com.batuhanseyrek.rezarvasyonSistemi.entity.adminEntity.Admin;
import com.batuhanseyrek.rezarvasyonSistemi.repository.AdminRepository;
import com.batuhanseyrek.rezarvasyonSistemi.security.JwtUtil;
import com.batuhanseyrek.rezarvasyonSistemi.service.admin.AdminService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    public DtoConverter dtoConverter;
    @Autowired
    public PasswordEncoder passwordEncoder;
    @Autowired
    public AuthenticationManager authManager;
    @Autowired
    public JwtUtil jwtUtil;
    @Autowired
    public AdminRepository adminRepository;


    @Override
    public Admin myApp(HttpServletRequest httpRequest) {
        Object attr = httpRequest.getAttribute("adminId"); // <-- Interceptor'dan gelen değer
        Admin admin = adminRepository.findById((Long) attr)
                .orElseThrow(() -> new RuntimeException("Admin bulunamadı"));
        Admin newadmin=new Admin();
        newadmin.setAdminName(admin.getAdminName());
        newadmin.setId(admin.getId());
        newadmin.setChairCount(admin.getChairCount());
        newadmin.setPassword(passwordEncoder.encode(admin.getPassword()));
        newadmin.setStoreName(admin.getStoreName());
        return newadmin;
    }

    @Override
    public Map<String,Object> mapping(AuthRequest request){
        try{
            authManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(),request.getPassword()));
        }
        catch (Exception e){
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", HttpStatus.UNAUTHORIZED.value()); // 401
            errorResponse.put("error",e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse).getBody();
        }
        String token = jwtUtil.generateToken(request.getUsername());
        Optional<Admin> admin= adminRepository.findByAdminName(request.getUsername());
        Map<String,Object> reponseMap=new HashMap<>();
        reponseMap.put("token",token);
        reponseMap.put("name",request.getUsername());
        reponseMap.put("id",admin.get().getId());
        return reponseMap;
    }
    @Override
    public ResponseEntity<String> register(Admin request){
        if (adminRepository.findByAdminName(request.getAdminName()).isPresent()){
            return ResponseEntity.badRequest().body("İsim önden kullanılmış");
        }
        Admin admin=new Admin();
        admin.setAdminName(request.getAdminName());
        admin.setPassword(passwordEncoder.encode(request.getPassword()));
        admin.setChairCount(0);
        admin.setStoreName(request.getStoreName());
        adminRepository.save(admin);
        return ResponseEntity.ok("Kayıt başarılı");

    }
    @Override
    public List<DtoAdmin> adminList(){
        List<Admin> admin = adminRepository.findAll();
        return admin.stream().map(DtoConverter::toDto).collect(Collectors.toList());
    }
    @Override
    public void adminDelete(Long id){
        adminRepository.deleteById(id);
    }

    @Override
    public ResponseEntity<?> adminUpdate(Admin request, HttpServletRequest httpRequest){
        Long adminId = null;
        Object attr = httpRequest.getAttribute("adminId"); // <-- Interceptor'dan gelen değer
        Admin admin = adminRepository.findById((Long) attr)
                .orElseThrow(() -> new RuntimeException("Admin bulunamadı"));
        admin.setAdminName(request.getAdminName());
        admin.setPassword(passwordEncoder.encode(request.getPassword()));
        admin.setChairCount(request.getChairCount());
        admin.setStoreName(request.getStoreName());
        adminRepository.save(admin);
        return ResponseEntity.ok("işlem başarılı");
    }
}
