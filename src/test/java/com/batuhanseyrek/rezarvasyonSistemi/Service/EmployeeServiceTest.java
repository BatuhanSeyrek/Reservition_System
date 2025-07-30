package com.batuhanseyrek.rezarvasyonSistemi.Service;


import com.batuhanseyrek.rezarvasyonSistemi.dto.DtoConverter;
import com.batuhanseyrek.rezarvasyonSistemi.dto.response.DtoChair;
import com.batuhanseyrek.rezarvasyonSistemi.dto.response.DtoEmployee;
import com.batuhanseyrek.rezarvasyonSistemi.entity.adminEntity.Employee;
import com.batuhanseyrek.rezarvasyonSistemi.repository.EmployeeRepository;
import com.batuhanseyrek.rezarvasyonSistemi.service.admin.EmployeeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {
    @InjectMocks
    private EmployeeService employeeService;
    @Mock
    private EmployeeRepository employeeRepository;
    void testEmployeeList_returnsListOfDtoEmployees(){
        Employee employee1= new Employee();
        employee1.setEmployeeName("Batu1");
        employee1.setId(1L);
        Employee employee2=new Employee();
        employee2.setId(2L);
        employee2.setEmployeeName("Batu2");
        List<Employee> employees= List.of(employee1,employee2);
        DtoEmployee dto1=new DtoEmployee();
        dto1.setEmployeeName("Batu1");
        DtoEmployee dto2=new DtoEmployee();
        dto2.setEmployeeName("Batu2");
        Mockito.when(employeeRepository.findAll()).thenReturn(employees);
        try (MockedStatic<DtoConverter> mockStatic = Mockito.mockStatic(DtoConverter.class)) {
            mockStatic.when(() -> DtoConverter.toDto(employee1)).thenReturn(dto1);
            mockStatic.when(() -> DtoConverter.toDto(employee2)).thenReturn(dto2);

            List<DtoEmployee> result=employeeService.employeeList();

            assertEquals(2, result.size());
            assertEquals("Masa 1",result.get(0).getEmployeeName());
            assertEquals("Masa 2",result.get(1).getEmployeeName());

            verify(employeeRepository).findAll();

            mockStatic.verify(() -> DtoConverter.toDto(employee1));
            mockStatic.verify(() -> DtoConverter.toDto(employee2));

        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
