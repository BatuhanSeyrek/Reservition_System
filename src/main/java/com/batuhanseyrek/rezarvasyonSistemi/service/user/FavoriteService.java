package com.batuhanseyrek.rezarvasyonSistemi.service.user;

import com.batuhanseyrek.rezarvasyonSistemi.dto.response.DtoStore;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface FavoriteService {
    void toggleFavorite(Long storeId, HttpServletRequest request);
    List<DtoStore> getUserFavorites(HttpServletRequest request);
}
