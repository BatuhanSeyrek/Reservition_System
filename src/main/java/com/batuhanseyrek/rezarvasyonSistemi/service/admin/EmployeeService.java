package com.batuhanseyrek.rezarvasyonSistemi.service.admin;

import com.batuhanseyrek.rezarvasyonSistemi.dto.response.DtoEmployee;
import com.batuhanseyrek.rezarvasyonSistemi.entity.adminEntity.Employee;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface EmployeeService {
    DtoEmployee employeeAdd(DtoEmployee request, HttpServletRequest httpRequest);
    List<DtoEmployee> employeeList();
    void employeeDelete(Long id);
    ResponseEntity<?> employeeUpdate(Long id, Employee request);
    List<DtoEmployee> getEmployeeByAdmin( HttpServletRequest httpServletRequest);
}
