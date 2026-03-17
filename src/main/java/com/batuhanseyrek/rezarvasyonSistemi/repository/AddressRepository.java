package com.batuhanseyrek.rezarvasyonSistemi.repository;

import com.batuhanseyrek.rezarvasyonSistemi.entity.adminEntity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address,Long> {

    Optional<Object> findByAdmin_Id(Long attr);
}
