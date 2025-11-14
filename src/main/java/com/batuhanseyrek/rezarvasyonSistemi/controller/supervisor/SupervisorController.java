package com.batuhanseyrek.rezarvasyonSistemi.controller.supervisor;

import com.batuhanseyrek.rezarvasyonSistemi.dto.request.AuthRequest;
import com.batuhanseyrek.rezarvasyonSistemi.dto.response.DtoAdmin;
import com.batuhanseyrek.rezarvasyonSistemi.entity.adminEntity.Admin;
import com.batuhanseyrek.rezarvasyonSistemi.entity.adminEntity.DtoRegisterAdmin;
import com.batuhanseyrek.rezarvasyonSistemi.service.admin.AdminService;
import com.batuhanseyrek.rezarvasyonSistemi.service.admin.Impl.ChairServiceImpl;
import com.batuhanseyrek.rezarvasyonSistemi.service.admin.SupervisorService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
@CrossOrigin(origins = "")
@RestController
@RequestMapping("/supervisor")
public class SupervisorController {

    @Autowired
    private SupervisorService supervisorService;
    private final AuthenticationManager authManager ;

    public SupervisorController( AuthenticationManager authManager) {

        this.authManager = authManager;
    }

    @GetMapping("/adminList")
    public List<DtoRegisterAdmin> adminList(){
        return supervisorService.adminList();
    }
    @PutMapping("/statusChange/{id}")
    public void statusChange(@PathVariable Long id){
        supervisorService.statusChange(id);
    }
   @PutMapping("/increaseDate/{id}")
    public void increaseDate(@PathVariable Long id){
        supervisorService.increaseDate(id);
    }
  /*  @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody DtoRegisterAdmin request){
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
    public void adminUpdate(@RequestBody DtoRegisterAdmin admin, HttpServletRequest httpRequest){
        adminLoginRegisterService.adminUpdate(admin,httpRequest);
    }*/
}
