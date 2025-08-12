package com.batuhanseyrek.rezarvasyonSistemi.controller.admin;

import com.batuhanseyrek.rezarvasyonSistemi.dto.DtoConverter;
import com.batuhanseyrek.rezarvasyonSistemi.dto.response.DtoChair;
import com.batuhanseyrek.rezarvasyonSistemi.entity.adminEntity.Chair;
import com.batuhanseyrek.rezarvasyonSistemi.repository.ChairRepository;
import com.batuhanseyrek.rezarvasyonSistemi.service.admin.ChairService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin/chair")
public class ChairController {
    @Autowired
    private ChairRepository chairRepository;
    @Autowired
    private ChairService adminSkillsService;
    @PostMapping(path = "/chairAdd")
    public DtoChair chairAdd(@RequestBody DtoChair request, HttpServletRequest httpRequest){
        return adminSkillsService.chairAdd(request,httpRequest);
    }
    @GetMapping(path = "/list")
    public List<DtoChair> chairlist(){
        return adminSkillsService.chairList();
    }
    @DeleteMapping(path = "/delete/{id}")
    public void chairdelete(@PathVariable Long id){
        adminSkillsService.chairDelete(id);
    }
    @PutMapping(path = "/update/{id}")
    public ResponseEntity<?> chairUpdate(@PathVariable Long id , @RequestBody Chair chair){
        return adminSkillsService.chairUpdate(id,chair);
    }

    @GetMapping("/chairget")
    public List<DtoChair> getChairsByAdmin( HttpServletRequest httpServletRequest) {
        return adminSkillsService.getChairsByAdmin(httpServletRequest);
    }

}
