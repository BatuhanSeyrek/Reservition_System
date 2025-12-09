package com.batuhanseyrek.rezarvasyonSistemi.repository;

import com.batuhanseyrek.rezarvasyonSistemi.entity.adminEntity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
@Repository
public interface AdminRepository extends JpaRepository<Admin,Long> {
    Optional<Admin> findByAdminName(String adminname);
    List<Admin> findByStatusAndEndTimeAfter(boolean status, LocalDateTime now);

    Optional<Admin> findByReferenceId(String referenceId);
}
