package com.batuhanseyrek.rezarvasyonSistemi.repository;

import com.batuhanseyrek.rezarvasyonSistemi.entity.adminEntity.Admin;
import com.batuhanseyrek.rezarvasyonSistemi.entity.adminEntity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface StoreRepository extends JpaRepository<Store,Long> {


}
