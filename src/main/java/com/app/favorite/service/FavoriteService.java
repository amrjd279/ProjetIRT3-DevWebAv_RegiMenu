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

    public List<FavoriteMeal> getAllFavorites(Long userProfileId) {
        return favoriteJdbcRepository.findAll(userProfileId);
    }

    public void addFavorite(Long userProfileId, Long recipeId, String title, String image) {
        favoriteJdbcRepository.save(userProfileId, recipeId, title, image);
    }

    public void removeFavorite(Long userProfileId, Long recipeId) {
        favoriteJdbcRepository.deleteByRecipeId(userProfileId, recipeId);
    }
}