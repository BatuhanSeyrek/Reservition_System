package com.batuhanseyrek.rezarvasyonSistemi.service.admin;

import com.batuhanseyrek.rezarvasyonSistemi.dto.DtoConverter;
import com.batuhanseyrek.rezarvasyonSistemi.dto.response.DtoEmployee;
import com.batuhanseyrek.rezarvasyonSistemi.entity.adminEntity.Admin;
import com.batuhanseyrek.rezarvasyonSistemi.entity.adminEntity.Chair;
import com.batuhanseyrek.rezarvasyonSistemi.entity.adminEntity.Employee;
import com.batuhanseyrek.rezarvasyonSistemi.repository.AdminRepository;
import com.batuhanseyrek.rezarvasyonSistemi.repository.ChairRepository;
import com.batuhanseyrek.rezarvasyonSistemi.repository.EmployeeRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private ChairRepository chairRepository;
    public DtoEmployee employeeAdd(DtoEmployee request, HttpServletRequest httpRequest) {
        Object attr = httpRequest.getAttribute("adminId");
        if (attr == null) {
            throw new RuntimeException("Admin ID bulunamadı");
        }
        Long adminId = (Long) attr;

        // 2. Admin'i bul
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin bulunamadı"));

        // 3. Chair nesnesini getir
        Chair chair = chairRepository.findById(Long.valueOf(request.getChairId()))
                .orElseThrow(() -> new RuntimeException("Koltuk bulunamadı."));
        List<Employee> employees=employeeRepository.findAll();
        int deger=0;
        for (Employee employee: employees){
            deger++;
        }
        if (deger < admin.getChairCount()) {
            Employee employee = new Employee();
            employee.setEmployeeName(request.getEmployeeName());
            employee.setChair(chair);
            employee.setAdmin(admin);

            // 5. Kaydet ve DTO olarak döndür
            Employee savedEmployee = employeeRepository.save(employee);
            return DtoConverter.toDto(savedEmployee);
        }
        else {
            throw new RuntimeException("Employee için maksimum sandalye sayısına ulaşıldı.");
        }

    }

    public List<DtoEmployee> employeeList(){
        List<Employee> employees = employeeRepository.findAll();
        return employees.stream()
                .map(DtoConverter::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void employeeDelete(Long id){
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        // Chair ilişkisi varsa önce kopar
        if (employee.getChair() != null) {
            Chair chair = employee.getChair();
            chair.setEmployee(null); // iki yönlü ilişki ise bunu da yap
            employee.setChair(null);
            chairRepository.save(chair);
        }

        employeeRepository.delete(employee);
    }

    public ResponseEntity<?> employeeUpdate(Long id, Employee request) {
        Optional<Employee> existingEmployeeOptional = employeeRepository.findById(id);

        if (existingEmployeeOptional.isPresent()) {
            Employee employee = existingEmployeeOptional.get();

            if (request.getEmployeeName() != null) {
                employee.setEmployeeName(request.getEmployeeName());
            }
            if (request.getChair() != null) {
                employee.setChair(request.getChair());
            }
            if (request.getAdmin() != null) {
                employee.setAdmin(request.getAdmin());
            }

            employeeRepository.save(employee);
            return ResponseEntity.ok("Çalışan başarıyla güncellendi.");
        }

        return ResponseEntity.badRequest().body("Böyle bir kullanıcı yok.");
    }


}
