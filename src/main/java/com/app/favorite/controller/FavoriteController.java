package com.app.favorite.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import com.app.favorite.entity.FavoriteMeal;
import com.app.favorite.service.FavoriteService;
import com.app.security.SecurityUser;

@RestController
@RequestMapping("/api/favorites")
@CrossOrigin(origins = "*")
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @GetMapping
    public ResponseEntity<List<FavoriteMeal>> getAllFavorites(
            @AuthenticationPrincipal SecurityUser currentUser) {
        List<FavoriteMeal> favorites =
                favoriteService.getAllFavorites(currentUser.getId());
        return ResponseEntity.ok(favorites);
    }

    @PostMapping("/{recipeId}")
    public ResponseEntity<Void> addFavorite(
            @AuthenticationPrincipal SecurityUser currentUser,
            @PathVariable Long recipeId,
            @RequestBody(required = false) Map<String, String> payload) {
        
        String title = (payload != null && payload.containsKey("title")) ? payload.get("title") : "Recette " + recipeId;
        String image = (payload != null && payload.containsKey("image")) ? payload.get("image") : "";

        favoriteService.addFavorite(
                currentUser.getId(), recipeId, title, image);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{recipeId}")
    public ResponseEntity<Void> removeFavorite(
            @AuthenticationPrincipal SecurityUser currentUser,
            @PathVariable Long recipeId) {
        favoriteService.removeFavorite(currentUser.getId(), recipeId);
        return ResponseEntity.ok().build();
    }
}