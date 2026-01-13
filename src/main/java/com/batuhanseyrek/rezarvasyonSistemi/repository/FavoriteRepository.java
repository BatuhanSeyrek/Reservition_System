package com.batuhanseyrek.rezarvasyonSistemi.repository;

import com.batuhanseyrek.rezarvasyonSistemi.entity.userEntity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    List<Favorite> findByUserId(Long userId);
    Optional<Favorite> findByUserIdAndStoreId(Long userId, Long storeId);
    boolean existsByUserIdAndStoreId(Long userId, Long storeId);
    void deleteByUserIdAndStoreId(Long userId, Long storeId);
}