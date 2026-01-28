package com.batuhanseyrek.rezarvasyonSistemi.service.admin.Impl;

import com.batuhanseyrek.rezarvasyonSistemi.dto.DtoConverter;
import com.batuhanseyrek.rezarvasyonSistemi.dto.request.AuthRequest;
import com.batuhanseyrek.rezarvasyonSistemi.dto.response.DtoAdmin;
import com.batuhanseyrek.rezarvasyonSistemi.dto.response.ReservationResponse;
import com.batuhanseyrek.rezarvasyonSistemi.entity.adminEntity.Address;
import com.batuhanseyrek.rezarvasyonSistemi.entity.adminEntity.Admin;
import com.batuhanseyrek.rezarvasyonSistemi.entity.adminEntity.DtoRegisterAdmin;
import com.batuhanseyrek.rezarvasyonSistemi.entity.adminEntity.Store;
import com.batuhanseyrek.rezarvasyonSistemi.repository.AddressRepository;
import com.batuhanseyrek.rezarvasyonSistemi.repository.AdminRepository;
import com.batuhanseyrek.rezarvasyonSistemi.repository.ReservationRepository;
import com.batuhanseyrek.rezarvasyonSistemi.repository.StoreRepository;
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

import java.time.LocalDateTime;
import java.time.LocalTime;
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
    ReservationRepository reservationRepository;
    @Autowired
    AddressRepository addressRepository;
    @Autowired
    public PasswordEncoder passwordEncoder;

    @Autowired
    public AuthenticationManager authManager;

    @Autowired
    public JwtUtil jwtUtil;

    @Autowired
    public AdminRepository adminRepository;

    @Autowired
    private StoreRepository storeRepository;
    @Override
    public DtoRegisterAdmin myApp(HttpServletRequest httpRequest) {
        Object attr = httpRequest.getAttribute("adminId");
        Admin admin = adminRepository.findById((Long) attr)
                .orElseThrow(() -> new RuntimeException("Admin bulunamadı"));
        Store store=storeRepository.findById((Long) attr).orElseThrow(() -> new RuntimeException("Store bulunamadı"));
        DtoRegisterAdmin newAdmin = new DtoRegisterAdmin();
        newAdmin.setId(admin.getId());
        newAdmin.setAdminName(admin.getAdminName());
        newAdmin.setEndTime(admin.getEndTime());
        newAdmin.setStartTime(admin.getStartTime());
        newAdmin.setPhoneNumber(admin.getPhoneNumber());
        newAdmin.setPassword(passwordEncoder.encode(admin.getPassword()));
        newAdmin.setStoreName(store.getStoreName());
        newAdmin.setChairCount(store.getChairCount());
        newAdmin.setReferenceId(admin.getReferenceId());
        newAdmin.setReferenceStatus(admin.isReferenceStatus());
        return newAdmin;
    }

    @Override
    public Map<String, Object> mapping(AuthRequest request) {
        try {
            authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", HttpStatus.UNAUTHORIZED.value());
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse).getBody();
        }

        String token = jwtUtil.generateToken(request.getUsername());
        Optional<Admin> adminOpt = adminRepository.findByAdminName(request.getUsername());
        if (adminOpt.isEmpty()) {
            // Admin bulunamadıysa hata döndür
            throw new RuntimeException("Kullanıcı bulunamadı");
        }

        Admin admin = adminOpt.get();

        if (admin.isStatus()==false){
            throw new RuntimeException("Kullanıcı aktif değil");
        }
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("token", token);
        responseMap.put("name", request.getUsername());
        responseMap.put("id", admin.getId());

        return responseMap;
    }

    @Override
    public ResponseEntity<String> register(DtoRegisterAdmin request) {
        if (adminRepository.findByAdminName(request.getAdminName()).isPresent()) {
            return ResponseEntity.badRequest().body("İsim önden kullanılmış");
        }
        Admin admin = new Admin();
        admin.setAdminName(request.getAdminName());
        admin.setPassword(passwordEncoder.encode(request.getPassword()));
        admin.setStatus(true);
        admin.setPhoneNumber(request.getPhoneNumber());
        admin.setStartTime(LocalDateTime.now()); // Şu anki tarih ve saat
        admin.setEndTime(LocalDateTime.now().plusDays(32)); // 32 gün sonrası
        // Store, chairs ve employees henüz eklenmemiş olabilir.
        admin.setStore(null);
        admin.setChairs(null);
        admin.setEmployees(null);

        adminRepository.save(admin);
        Store store =new Store();
        store.setChairCount(0);
        store.setStoreName(request.getStoreName());
        store.setAdmin(admin);
        storeRepository.save(store);
        Address address = new Address();
        address.setDistrict(request.getDistrict());
        address.setCity(request.getCity());
        address.setAdmin(admin);
        addressRepository.save(address);
        return ResponseEntity.ok("Kayıt başarılı");
    }

    @Override
    public List<DtoAdmin> adminList() {
        List<Admin> admins = adminRepository.findAll();
        return admins.stream().map(DtoConverter::toDto).collect(Collectors.toList());
    }

    @Override
    public void adminDelete(Long id) {
        adminRepository.deleteById(id);
    }

    @Override
    public ResponseEntity<?> adminUpdate(DtoRegisterAdmin request, HttpServletRequest httpRequest) {
        Object attr = httpRequest.getAttribute("adminId");
        Admin admin = adminRepository.findById((Long) attr)
                .orElseThrow(() -> new RuntimeException("Admin bulunamadı"));
        Store store = storeRepository.findById((Long) attr)
                .orElseThrow(() -> new RuntimeException("Store bulunamadı"));

        // Admin adı güncelle
        admin.setAdminName(request.getAdminName());
        if (admin.isReferenceStatus()==false){
        admin.setReferenceId(request.getReferenceId());
            if (!request.getReferenceId().isEmpty()){
                admin.setReferenceStatus(!admin.isReferenceStatus());
            }
        }

        // Şifre sadece doluysa güncellenecek
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            admin.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        adminRepository.save(admin);

        // Store adı güncelle
        store.setStoreName(request.getStoreName());
        storeRepository.save(store);

        return ResponseEntity.ok("İşlem başarılı");
    }

    @Override
    public List<ReservationResponse> getRezervationForMyAdmin(HttpServletRequest httpServletRequest) {
        Object attr = httpServletRequest.getAttribute("adminId");
        return reservationRepository.findAll().stream()
                .filter(r -> r.getStore().getId().equals(attr))  // sadece admin'in mağazası
                .map(DtoConverter::toDto)                           // DTO çevirme
                .collect(Collectors.toList());

    }
}
