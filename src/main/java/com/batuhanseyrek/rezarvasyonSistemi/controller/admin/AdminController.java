package com.batuhanseyrek.rezarvasyonSistemi.controller.admin;

import com.batuhanseyrek.rezarvasyonSistemi.dto.request.AuthRequest;
import com.batuhanseyrek.rezarvasyonSistemi.dto.response.DtoAdmin;
import com.batuhanseyrek.rezarvasyonSistemi.entity.adminEntity.Admin;
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
    private AdminService adminLoginRegisterService;
    private final AuthenticationManager authManager ;

    public AdminController( AuthenticationManager authManager) {

        this.authManager = authManager;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody AuthRequest request){
        return ResponseEntity.ok(adminLoginRegisterService.mapping(request));
    }
    @GetMapping("/myAdmin")
    public Admin myApp(HttpServletRequest httpServletRequest){
       return adminLoginRegisterService.myApp(httpServletRequest);
    }
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody Admin request){
        adminLoginRegisterService.register(request);
        return ResponseEntity.ok("Kayıt başarılı");
    }
    @GetMapping("/list")
    public List<DtoAdmin> adminList(){
        return adminLoginRegisterService.adminList();
    }
    @DeleteMapping("/delete/{id}")
    public void adminDelete(@PathVariable Long id){
        adminLoginRegisterService.adminDelete(id);
    }
    @PutMapping("/update")
    public void adminUpdate(@RequestBody Admin admin, HttpServletRequest httpRequest){
        adminLoginRegisterService.adminUpdate(admin,httpRequest);
    }
}
