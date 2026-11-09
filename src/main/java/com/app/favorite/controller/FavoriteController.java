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

import com.app.favorite.entity.FavoriteMeal;
import com.app.favorite.service.FavoriteService;

@RestController
@RequestMapping("/api/favorites")
@CrossOrigin(origins = "*")
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @GetMapping
    public ResponseEntity<List<FavoriteMeal>> getAllFavorites() {
        List<FavoriteMeal> favorites = favoriteService.getAllFavorites();
        return ResponseEntity.ok(favorites);
    }

    @PostMapping("/{recipeId}")
    public ResponseEntity<Void> addFavorite(
            @PathVariable Long recipeId,
            @RequestBody(required = false) Map<String, String> payload) {
        
        String title = (payload != null && payload.containsKey("title")) ? payload.get("title") : "Recette " + recipeId;
        String image = (payload != null && payload.containsKey("image")) ? payload.get("image") : "";

        favoriteService.addFavorite(recipeId, title, image);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{recipeId}")
    public ResponseEntity<Void> removeFavorite(@PathVariable Long recipeId) {
        favoriteService.removeFavorite(recipeId);
        return ResponseEntity.ok().build();
    }
}