package com.batuhanseyrek.rezarvasyonSistemi.service.user.Impl;

import com.batuhanseyrek.rezarvasyonSistemi.dto.DtoConverter;
import com.batuhanseyrek.rezarvasyonSistemi.dto.response.DtoStore;
import com.batuhanseyrek.rezarvasyonSistemi.entity.adminEntity.Store;
import com.batuhanseyrek.rezarvasyonSistemi.entity.userEntity.Favorite;
import com.batuhanseyrek.rezarvasyonSistemi.entity.userEntity.User;
import com.batuhanseyrek.rezarvasyonSistemi.repository.FavoriteRepository;
import com.batuhanseyrek.rezarvasyonSistemi.repository.StoreRepository;
import com.batuhanseyrek.rezarvasyonSistemi.repository.UserRepository;
import com.batuhanseyrek.rezarvasyonSistemi.service.user.FavoriteService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FavoriteServiceImpl implements FavoriteService {

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Override
    @Transactional
    public void toggleFavorite(Long storeId, HttpServletRequest request) {
        // HttpServletRequest'ten userId alımı (senin sistemindeki yapı)
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) throw new RuntimeException("Kullanıcı girişi gerekli.");

        Optional<Favorite> existingFavorite = favoriteRepository.findByUserIdAndStoreId(userId, storeId);

        if (existingFavorite.isPresent()) {
            // Varsa favoriden çıkar
            favoriteRepository.delete(existingFavorite.get());
        } else {
            // Yoksa favoriye ekle
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));
            Store store = storeRepository.findById(storeId)
                    .orElseThrow(() -> new RuntimeException("Mağaza bulunamadı"));

            Favorite favorite = new Favorite();
            favorite.setUser(user);
            favorite.setStore(store);
            favoriteRepository.save(favorite);
        }
    }

    @Override
    public List<DtoStore> getUserFavorites(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) return new ArrayList<>();

        List<Favorite> favorites = favoriteRepository.findByUserId(userId);

        return favorites.stream()
                .map(f -> DtoConverter.toDto(f.getStore()))
                .collect(Collectors.toList());
    }
}