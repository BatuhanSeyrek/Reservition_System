package com.batuhanseyrek.rezarvasyonSistemi.repository;

import com.batuhanseyrek.rezarvasyonSistemi.dto.response.DtoEmployee;
import com.batuhanseyrek.rezarvasyonSistemi.entity.adminEntity.Chair;
import com.batuhanseyrek.rezarvasyonSistemi.entity.adminEntity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findByAdmin_Id(Long adminId);
}

