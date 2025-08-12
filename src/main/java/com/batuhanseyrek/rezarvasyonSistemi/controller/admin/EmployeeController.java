package com.batuhanseyrek.rezarvasyonSistemi.controller.admin;

import com.batuhanseyrek.rezarvasyonSistemi.dto.response.DtoChair;
import com.batuhanseyrek.rezarvasyonSistemi.dto.response.DtoEmployee;
import com.batuhanseyrek.rezarvasyonSistemi.entity.adminEntity.Employee;
import com.batuhanseyrek.rezarvasyonSistemi.service.admin.EmployeeService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;
    @PostMapping(path = "/employeeAdd")
    public DtoEmployee employeeAdd(@RequestBody DtoEmployee dtoEmployee, HttpServletRequest httpRequest){
        return employeeService.employeeAdd(dtoEmployee,httpRequest);
    }
    @GetMapping(path = "/list")
    public List<DtoEmployee> employeeList(){
        return employeeService.employeeList();
    }
    @DeleteMapping(path = "/delete/{id}")
    public void employeeDelete(@PathVariable Long id){
        employeeService.employeeDelete(id);
    }
    @PutMapping(path = "/update/{id}")
    public ResponseEntity<?> employeeUpdate(@PathVariable Long id, @RequestBody Employee employee){
        employeeService.employeeUpdate(id,employee);
        return ResponseEntity.ok("Güncelleme işlemleri tamamlanmıştır");
    }
    @GetMapping("/employeeget")
    public List<DtoEmployee> getEmployeeByAdmin( HttpServletRequest httpServletRequest) {
        return employeeService.getEmployeeByAdmin(httpServletRequest);
    }

}
