package com.batuhanseyrek.rezarvasyonSistemi.controller.admin;

import com.batuhanseyrek.rezarvasyonSistemi.dto.request.AuthRequest;
import com.batuhanseyrek.rezarvasyonSistemi.dto.response.DtoAdmin;
import com.batuhanseyrek.rezarvasyonSistemi.dto.response.ReservationResponse;
import com.batuhanseyrek.rezarvasyonSistemi.entity.adminEntity.Admin;
import com.batuhanseyrek.rezarvasyonSistemi.entity.adminEntity.DtoRegisterAdmin;
import com.batuhanseyrek.rezarvasyonSistemi.service.admin.AdminService;
import com.batuhanseyrek.rezarvasyonSistemi.service.admin.Impl.ChairServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
@CrossOrigin(origins = "")
@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private ChairServiceImpl adminSkillsService;
    @Autowired
    private AdminService AdminService;
    private final AuthenticationManager authManager ;

    public AdminController( AuthenticationManager authManager) {

        this.authManager = authManager;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody AuthRequest request){
        return ResponseEntity.ok(AdminService.mapping(request));
    }
    @GetMapping("/myAdmin")
    public DtoRegisterAdmin myApp(HttpServletRequest httpServletRequest){
       return AdminService.myApp(httpServletRequest);
    }
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody DtoRegisterAdmin request){
        AdminService.register(request);
        return ResponseEntity.ok("Kayıt başarılı");
    }
    @GetMapping("/list")
    public List<DtoAdmin> adminList(){
        return AdminService.adminList();
    }
    @DeleteMapping("/delete/{id}")
    public void adminDelete(@PathVariable Long id){
        AdminService.adminDelete(id);
    }
    @PutMapping("/update")
    public void adminUpdate(@RequestBody DtoRegisterAdmin admin, HttpServletRequest httpRequest){
        AdminService.adminUpdate(admin,httpRequest);
    }
    @GetMapping("/getRezervationForMyAdmin")
    public List<ReservationResponse> getRezervationForMyAdmin(HttpServletRequest httpServletRequest) {
        return AdminService.getRezervationForMyAdmin(httpServletRequest);
    }
}
