package com.app.favorite.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.app.favorite.entity.FavoriteMeal;
import com.app.favorite.repository.FavoriteJdbcRepository;

@Service
public class FavoriteService {

    private final FavoriteJdbcRepository favoriteJdbcRepository;

    public FavoriteService(FavoriteJdbcRepository favoriteJdbcRepository) {
        this.favoriteJdbcRepository = favoriteJdbcRepository;
    }

    public List<FavoriteMeal> getAllFavorites() {
        return favoriteJdbcRepository.findAll();
    }

    public void addFavorite(Long recipeId, String title, String image) {
        favoriteJdbcRepository.save(recipeId, title, image);
    }

    public void removeFavorite(Long recipeId) {
        favoriteJdbcRepository.deleteByRecipeId(recipeId);
    }
}