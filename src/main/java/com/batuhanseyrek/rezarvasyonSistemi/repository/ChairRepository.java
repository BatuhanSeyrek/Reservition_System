package com.batuhanseyrek.rezarvasyonSistemi.repository;

import com.batuhanseyrek.rezarvasyonSistemi.entity.adminEntity.Admin;
import com.batuhanseyrek.rezarvasyonSistemi.entity.adminEntity.Chair;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChairRepository extends JpaRepository<Chair,Long> {
    Optional<Chair> findByChairName(String chairname);


    int countByStoreId(Long id);

    List<Chair> findByStoreId(Long storeId);


}
