package com.app.clientapi;

import com.app.clientapi.dto.SpoonacularResponseDto;
import com.app.clientapi.mapper.RecipeMapper;
import com.app.menu.model.MealDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

/**
 * Implémentation de RecipeGateway basée sur le RestClient de Spring Boot 3.2+.
 */
@Component
public class SpoonacularRestClient implements RecipeGateway {

    private static final Logger log = LoggerFactory.getLogger(SpoonacularRestClient.class);

    private final RestClient restClient;
    private final RecipeMapper recipeMapper;
    private final String apiKey;

    public SpoonacularRestClient(RestClient spoonacularRestClientInstance,
                                 RecipeMapper recipeMapper,
                                 @Value("${spoonacular.api.key}") String apiKey) {
        this.restClient = spoonacularRestClientInstance;
        this.recipeMapper = recipeMapper;
        this.apiKey = apiKey;
    }

    @Override
    public List<MealDto> getRandomRecipes(int count) {
        log.debug("Appel externe Spoonacular pour récupérer {} recettes aléatoires", count);

        SpoonacularResponseDto rawResponse = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/recipes/random")
                        .queryParam("number", count)
                        .queryParam("apiKey", apiKey)
                        .build())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                    log.error("Erreur client Spoonacular: status {}", response.getStatusCode());
                    throw new RuntimeException("Erreur de requête vers l'API Spoonacular");
                })
                .onStatus(HttpStatusCode::is5xxServerError, (request, response) -> {
                    log.error("Erreur serveur Spoonacular: status {}", response.getStatusCode());
                    throw new RuntimeException("API Spoonacular indisponible");
                })
                .body(SpoonacularResponseDto.class);

        return recipeMapper.toMealDtoList(rawResponse);
    }
}

