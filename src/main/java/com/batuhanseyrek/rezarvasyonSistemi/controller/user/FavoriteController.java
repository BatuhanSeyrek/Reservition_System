package com.batuhanseyrek.rezarvasyonSistemi.controller.user;

import com.batuhanseyrek.rezarvasyonSistemi.dto.response.DtoStore;
import com.batuhanseyrek.rezarvasyonSistemi.service.user.FavoriteService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;

    @PostMapping("/toggle/{storeId}")
    public ResponseEntity<String> toggleFavorite(@PathVariable Long storeId, HttpServletRequest request) {
        favoriteService.toggleFavorite(storeId, request);
        return ResponseEntity.ok("Favori durumu güncellendi.");
    }

    @GetMapping("/my-favorites")
    public ResponseEntity<List<DtoStore>> getMyFavorites(HttpServletRequest request) {
        return ResponseEntity.ok(favoriteService.getUserFavorites(request));
    }
}